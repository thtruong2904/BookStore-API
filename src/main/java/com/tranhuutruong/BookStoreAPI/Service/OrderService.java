package com.tranhuutruong.BookStoreAPI.Service;

import com.itextpdf.text.*;
import com.itextpdf.text.Font;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.*;
import com.tranhuutruong.BookStoreAPI.Interface.OrderInterface;
import com.tranhuutruong.BookStoreAPI.Model.*;
import com.tranhuutruong.BookStoreAPI.Model.User.AccountModel;
import com.tranhuutruong.BookStoreAPI.Model.User.UserInfoModel;
import com.tranhuutruong.BookStoreAPI.Repository.*;
import com.tranhuutruong.BookStoreAPI.Repository.User.AccountRepository;
import com.tranhuutruong.BookStoreAPI.Repository.User.UserInfoRepository;
import com.tranhuutruong.BookStoreAPI.Request.OrderDetailRequest;
import com.tranhuutruong.BookStoreAPI.Request.OrderRequest;
import com.tranhuutruong.BookStoreAPI.Response.ApiResponse;
import com.tranhuutruong.BookStoreAPI.Utils.CurrentDateTime;
import com.tranhuutruong.BookStoreAPI.Utils.Format;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.internet.MimeMessage;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService implements OrderInterface {

    @Autowired
    private JavaMailSender javaMailSender;
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    @Autowired
    private ProductDetailRepository productDetailRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private ReceiveRepository receiveRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public ApiResponse<Object> getAllOrder() {
        List<OrderModel> listOrder = orderRepository.findAll();
        return ApiResponse.builder().status(200).message("Danh sách đơn đặt hàng").data(listOrder).build();
    }

    @Override
    public ApiResponse<Object> order(String username, Long receive_id, OrderRequest orderRequest) {
        String message = "";
        OrderModel newOrder = null;
        AccountModel accountModel = accountRepository.findAccountModelByUsername(username);
        Optional<UserInfoModel> userInfoModel = Optional.ofNullable(userInfoRepository.findUserInfoModelByAccountModel(accountModel));
        if(!userInfoModel.isPresent())
        {
            return ApiResponse.builder().status(101).message("Không tìm thấy người dùng").build();
        }
        else {
            try {
                newOrder = OrderModel.builder()
                        .userInfoModel(userInfoModel.get())
                        .statusModel(statusRepository.findById(1L).get())
                        .note(orderRequest.getNote())
                        .feeShip(orderRequest.getFeeShip())
                        .date(CurrentDateTime.getCurrentDateTime())
                        .receiveModel(receiveRepository.findById(receive_id).get())
                        .address(orderRequest.getAddress()).build();

                orderRepository.save(newOrder);

                OrderModel finalNewOrder = newOrder;
                MimeMessage mailMessage = javaMailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(mailMessage, true, "UTF-8");
                for(OrderDetailRequest item : orderRequest.getListProduct()){
                    Optional<ProductDetailModel> productDetailModel = productDetailRepository.findById(item.getProduct_id());
                    if(productDetailModel.get().getCurrent_number() < item.getAmount())
                        throw new Exception("Vấn đề tồn kho số lượng sản phẩm "+productDetailModel.get().getProductModel().getName() +" của chúng tôi không đủ!");
                    CartModel checkCart = cartRepository.findCartModelByUserInfoModel_AccountModel_UsernameAndProductDetail_Id(username, item.getProduct_id());
                    if(checkCart != null && checkCart.getId() > 0)
                        cartRepository.delete(checkCart);

                    OrderDetailModel orderDetailModel = OrderDetailModel.builder()
                            .productDetailModel(productDetailModel.get())
                            .orderModel(finalNewOrder)
                            .amount(item.getAmount())
                            .price(productDetailModel.get().getProductModel().getPrice()).build();
                    orderDetailRepository.save(orderDetailModel);

                    helper.setTo(userInfoModel.get().getAccountModel().getEmail());
                    helper.setSubject("Xác nhận đơn hàng");
                    helper.setFrom("tranhuutruong290401@gmail.com");
                    String productDetail = "";
                    Long totalPrices = 0L;
                    int i = 0;
                    for(OrderDetailRequest item1 : orderRequest.getListProduct())
                    {
                        Optional<ProductDetailModel> productDetailModel1 = productDetailRepository.findById(item1.getProduct_id());
                        productDetail += "<p>" + ++i + ": " + productDetailModel1.get().getProductModel().getName() + " - "
                                + productDetailModel1.get().getProductModel().getDescription() + " - "
                                + productDetailModel1.get().getSize() + " - "
                                + productDetailModel1.get().getColor() + " - "
                                + "Giá: " + productDetailModel1.get().getProductModel().getPrice() + " đ" + " - "
                                + "Số lượng: " + item1.getAmount() +"</p>";
                        totalPrices += productDetailModel1.get().getProductModel().getPrice();
                    }
                    totalPrices += newOrder.getFeeShip();
                    helper.setText("<html><body><p>Xin chào! " + userInfoModel.get().getFirstname() + " " + userInfoModel.get().getLastname()
                            + "</p><p>Bạn đã đặt một đơn hàng qua Website của chúng tôi! Vui lòng bấm vào link bên dưới để xác nhận đơn hàng</p>"
                            + "<p>ĐƠN HÀNG: số " + newOrder.getId() + "</p>"
                            + "<p>Danh sách sản phẩm :" + productDetail + "</p>"
                            + "<p> Phí ship : " + newOrder.getFeeShip() + " đ"
                            + "<h4>Tổng tiền: " + totalPrices + " đ" + "</h4>"
                            + "<h4>Địa chỉ nhận hàng: " + newOrder.getAddress() + "</h4>"
                            + "<p>Vui lòng bấm vào link sau để xác nhận đơn hàng</p>" + "http://localhost:8080/api/order/confirm-order?id-order=" + newOrder.getId()
                    , true);
                }
                emailService.sendEmail(mailMessage);
                message= "Đơn hàng đươc tạo thành công";
            }
            catch (Exception e)
            {
                message = "Lỗi tạo đơn hàng";
//                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                return ApiResponse.builder().status(101).message(message).data(null).build();
            }
        }
        return ApiResponse.builder().status(200).message(message).data(newOrder).build();
    }

    @Override
    public ModelAndView confirmOrder(ModelAndView modelAndView, Long order_id)
    {
        Optional<OrderModel> orderModel = orderRepository.findById(order_id);
        if(orderModel.isPresent())
        {
            if(orderModel.get().getStatusModel().getName().equals("Đang giao hàng"))
            {
                modelAndView.setViewName("SuccessOrder");
                return modelAndView;
            }
            else if(orderModel.get().getStatusModel().getName().equals("Đã nhận hàng"))
            {
                modelAndView.setViewName("receivedOrder");
                return modelAndView;
            }
            else if(orderModel.get().getStatusModel().getName().equals("Đơn đã hủy"))
            {
                modelAndView.setViewName("CanceledOrder");
                return modelAndView;
            }
            else
            {
                List<OrderDetailModel> orderDetailModels = new ArrayList<>(orderModel.get().getOrderDetailModels());
                for (OrderDetailModel item : orderDetailModels) {
                    if (item.getAmount() > item.getProductDetailModel().getCurrent_number()) {
                        StatusModel statusModel = statusRepository.findByName("Đơn đã hủy");
                        orderModel.get().setStatusModel(statusModel);
                        orderRepository.save(orderModel.get());
                        modelAndView.setViewName("SoldOut");
                        return modelAndView;
                    } else {
                        Optional<ProductDetailModel> productDetailModel = productDetailRepository.findById(item.getProductDetailModel().getId());
                        productDetailModel.get().setCurrent_number(productDetailModel.get().getCurrent_number() - item.getAmount());
                        StatusModel statusModel = statusRepository.findByName("Đang giao hàng");
                        orderModel.get().setStatusModel(statusModel);
                        orderRepository.save(orderModel.get());
                        modelAndView.setViewName("confirmationOrder");
                    }
                }
            }
        }
        else {
            modelAndView.addObject("message","Error confirm Order!");
            modelAndView.setViewName("error");
        }
        return modelAndView;
    }

    @Override
    public ApiResponse<Object> confirmPayOrder(Long idOrder)
    {
        Optional<OrderModel> orderModel = orderRepository.findById(idOrder);
        if(!orderModel.isPresent() || orderModel.get().getId() <= 0)
        {
            return ApiResponse.builder().status(101).message("Có lỗi! Vui lòng thử lại").build();
        }
        StatusModel statusModel = statusRepository.findByName("Đã nhận hàng");
        orderModel.get().setStatusModel(statusModel);
        orderRepository.save(orderModel.get());
        return ApiResponse.builder().status(200).message("Đơn hàng đã được giao và thanh toán thành công!").data(orderModel.get()).build();
    }

    @Override
    public ApiResponse<Object> getOrderByStatus(String username, String status_name) {
        Iterable<OrderModel> orderModels = orderRepository.findAllByNameStatus(username, status_name);
        return ApiResponse.builder().status(200).message("Danh sách đơn theo status").data(orderModels).build();
    }

    @Override
    public ApiResponse<Object> cancelOrderByUser(String username, Long idOrder) {
        OrderModel orderModel = orderRepository.findBOrderByUserNameAndId(username, idOrder);
        if(orderModel == null || orderModel.getId() <= 0)
        {
            return ApiResponse.builder().status(101).message("Đơn hàng không tồn tại").build();
        }
        if(!orderModel.getStatusModel().getName().equals("Chờ xác nhận"))
        {
            return ApiResponse.builder().status(101).message("Đơn hàng " + orderModel.getStatusModel().getName() + " nên không thể hủy").data(orderModel).build();
        }
        StatusModel statusModel = statusRepository.findByName("Đơn đã hủy");
        orderModel.setStatusModel(statusModel);
        orderRepository.save(orderModel);
        return ApiResponse.builder().status(200).message("Đơn hàng đã được hủy").data(orderModel).build();
    }

    // gửi hóa đơn cho khách sau khi được xác nhận đã nhận hàng
    public ApiResponse<Object> electronicInvoice(String username, Long idOrder)
    {
        AccountModel accountModel = accountRepository.findAccountModelByUsername(username);
        UserInfoModel userInfoModel = userInfoRepository.findUserInfoModelByAccountModel(accountModel);
        if(userInfoModel == null || userInfoModel.getId() <= 0)
        {
            return ApiResponse.builder().status(100).message("Không tìm thấy người dùng").build();
        }
        String message = "Hóa đơn đã được gửi!";
        int status = 200;
        Optional<OrderModel> orderModel = orderRepository.findById(idOrder);
        StatusModel statusModel = statusRepository.findByName("Đã nhận hàng");
        orderModel.get().setStatusModel(statusModel);
        orderRepository.save(orderModel.get());
        if(orderModel.isPresent())
        {
            try{
                Document document = new Document(PageSize.A4);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                PdfWriter pdfWriter = PdfWriter.getInstance(document, outputStream);
                document.open();

                // phần header
                float colWidth = 280f;
                PdfPTable table = new PdfPTable(2);
                table.setWidths(new float[]{colWidth, colWidth});
                table.setWidthPercentage(100);
                table.getDefaultCell().setBorder(PdfPCell.NO_BORDER);
                table.setPaddingTop(30f);

                BaseFont baseFont = BaseFont.createFont("src/main/resources/templates/vuArial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
                Font headerFont = new Font(baseFont, 20, Font.BOLD);

                PdfPCell cell1 = new PdfPCell(new Phrase("BOOKSTORE \nE-Invoice", headerFont));
                cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
                cell1.setBackgroundColor(new BaseColor(63, 169, 219));
                cell1.setBorder(PdfPCell.NO_BORDER);
                table.addCell(cell1);

                Font contentFont = new Font(baseFont, 12, Font.NORMAL);
                PdfPCell cell2 = new PdfPCell();
                cell2.addElement(new Phrase("Mã đơn hàng: " + orderModel.get().getId() + "\nNgày đặt: " + orderModel.get().getDate()
                        + "\nTên khách hàng: " + userInfoModel.getFirstname() + " " + userInfoModel.getLastname()
                        + "\nSố điện thoại: " + userInfoModel.getPhone(), contentFont));
                cell2.setHorizontalAlignment(Element.ALIGN_RIGHT);
                cell2.setBackgroundColor(new BaseColor(63, 169, 219));
                cell2.setPaddingRight(10f);
                cell2.setBorder(PdfPCell.NO_BORDER);
                table.addCell(cell2);

                // Tạo bảng phần thân
                PdfPTable table2 = new PdfPTable(5);
                table2.setWidthPercentage(100);
                table2.setSpacingBefore(10f);
                table2.setSpacingAfter(10f);

                float[] columnWidths = { 2f, 1f, 1f, 0.5f, 1f};
                table2.setWidths(columnWidths);
                Font fontNameColumn = new Font(baseFont, 12, Font.BOLD);

                // Tạo các ô trong bảng với font headerFont
                PdfPCell cell3 = new PdfPCell(new Phrase("Tên sản phẩm", fontNameColumn));
                PdfPCell cell4 = new PdfPCell(new Phrase("Kích thước", fontNameColumn));
                PdfPCell cell5 = new PdfPCell(new Phrase("Màu sắc", fontNameColumn));
                PdfPCell cell6 = new PdfPCell(new Phrase("Số lượng", fontNameColumn));
                PdfPCell cell7 = new PdfPCell(new Phrase("Giá( vnđ )", fontNameColumn));
                // Thêm các ô vào bảng
                table2.addCell(cell3);
                table2.addCell(cell4);
                table2.addCell(cell5);
                table2.addCell(cell6);
                table2.addCell(cell7);


                // Dữ liệu đơn hàng
                Font fontDetail = new Font(baseFont, 10);
                Long totalMoneyOrder = 0L;
                List<OrderDetailModel> orderDetailModelList = orderModel.get().getOrderDetailModels();
                for (OrderDetailModel item : orderDetailModelList) {
                    table2.addCell(new PdfPCell(new Phrase(item.getProductDetailModel().getProductModel().getName(), fontDetail)));
                    table2.addCell(new PdfPCell(new Phrase(item.getProductDetailModel().getSize(), fontDetail)));
                    table2.addCell(new PdfPCell(new Phrase(item.getProductDetailModel().getColor(), fontDetail)));
                    table2.addCell(new PdfPCell(new Phrase(item.getAmount().toString(), fontDetail)));
                    table2.addCell(new PdfPCell(new Phrase(Format.formatMoney(item.getProductDetailModel().getProductModel().getPrice()), fontDetail)));
                    totalMoneyOrder += (item.getAmount() * item.getProductDetailModel().getProductModel().getPrice());
                }

                PdfPCell cell8 = new PdfPCell(new Phrase("", fontDetail));
                PdfPCell cell9 = new PdfPCell(new Phrase("", fontDetail));
                PdfPCell cell10 = new PdfPCell(new Phrase("Phí ship: ", fontNameColumn));
                PdfPCell cell11 = new PdfPCell(new Phrase("", fontDetail));
                PdfPCell cell12 = new PdfPCell(new Phrase(Format.formatMoney(orderModel.get().getFeeShip()) + " vnđ", fontDetail));

                // Thêm các ô vào bảng
                table2.addCell(cell8).setBorder(Rectangle.NO_BORDER);
                table2.addCell(cell9).setBorder(Rectangle.NO_BORDER);
                table2.addCell(cell10).setBorder(Rectangle.NO_BORDER);
                table2.addCell(cell11).setBorder(Rectangle.NO_BORDER);
                table2.addCell(cell12).setBorder(Rectangle.NO_BORDER);


                PdfPCell cell13 = new PdfPCell(new Phrase("", fontDetail));
                PdfPCell cell14 = new PdfPCell(new Phrase("", fontDetail));
                PdfPCell cell15 = new PdfPCell(new Phrase("Tổng tiền: ", fontNameColumn));
                PdfPCell cell16 = new PdfPCell(new Phrase("", fontDetail));
                PdfPCell cell17 = new PdfPCell(new Phrase(Format.formatMoney(totalMoneyOrder + orderModel.get().getFeeShip()) + " vnđ", fontDetail));

                // Thêm các ô vào bảng
                table2.addCell(cell13).setBorder(Rectangle.NO_BORDER);
                table2.addCell(cell14).setBorder(Rectangle.NO_BORDER);
                table2.addCell(cell15).setBorder(Rectangle.NO_BORDER);
                table2.addCell(cell16).setBorder(Rectangle.NO_BORDER);
                table2.addCell(cell17).setBorder(Rectangle.NO_BORDER);

                Font fontTitle = new Font(baseFont, 14, Font.BOLD);
                Paragraph paragraph = new Paragraph("Địa chỉ nhận hàng: " + orderModel.get().getAddress() + "\nChi tiết đơn hàng", fontTitle);
                paragraph.setAlignment(Element.ALIGN_CENTER);

                document.add(table);
                document.add(paragraph); // Khoảng trắng
                document.add(table2);
                document.close();

                InputStreamSource attachment = new ByteArrayResource(outputStream.toByteArray());

                // Gửi email
                String subject = "BOOKSTORE hóa đơn điện tử";
                String setText = "BOOKSTORE gửi anh/chị " + userInfoModel.getFirstname() + " " + userInfoModel.getLastname() + " hóa đơn điện tử \n" +
                        "Cảm ơn vì đã luôn tin tưởng BOOKSTORE của chúng tôi!";
                emailService.sendEmailWithAttachment("tranhuutruong290401@gmail.com", accountModel.getEmail(), subject, setText, attachment);
            }
            catch (Exception e)
            {
                message = "Lỗi gửi hóa đơn!";
                status = 101;
                return ApiResponse.builder().message(message).status(status).build();
            }
        }
        return ApiResponse.builder().message(message).status(status).build();
    }

    @Override
    public Page<OrderModel> searchOrder(Date from, Date to, String query, String status, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("date").descending());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String fromDateString = dateFormat.format(from);
        String toDateString = dateFormat.format(to);
        return orderRepository.searchOrder(fromDateString, toDateString, query == "" ? null : query, status == "" ? null : status, pageable);
//        return orderRepository.searchOrder(from.toInstant(), to.toInstant(), query == "" ? null : query, status == "" ? null : status, pageable);
    }
}
