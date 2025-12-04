package com.example.duan1.services;

import com.example.duan1.model.CartItem;
import com.example.duan1.model.DanhMuc;
import com.example.duan1.model.Order;
import com.example.duan1.model.OrderStatusData;
import com.example.duan1.model.Product;
import com.example.duan1.model.RevenueData;
import com.example.duan1.model.Response;
import com.example.duan1.model.User;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiServices {
    // CẤU HÌNH IP:
    // - Nếu dùng Android EMULATOR: dùng "http://10.0.2.2:3000/" (localhost của máy)
    // - Nếu dùng thiết bị THẬT: dùng IP thật của máy "http://192.168.2.44:3000/"
    // - Lấy IP thật: Windows: ipconfig | Linux/Mac: ifconfig
    String Url = "http://10.0.2.2:3000/";  // IP đặc biệt cho Android Emulator (localhost của máy)
    
    // ==================== AUTH APIs ====================
    @POST("api/register")
    Call<Response<User>> register(@Body Map<String, String> body);
    
    @POST("api/login")
    Call<Response<User>> login(@Body Map<String, String> body);

    // ==================== PRODUCT APIs ====================
    @GET("api/products")
    Call<Response<List<Product>>> getProducts();
    
    @GET("api/products")
    Call<Response<List<Product>>> getAllProducts();
    
    @GET("api/products/{id}")
    Call<Response<Product>> getProductById(@Path("id") String id);
    
    @GET("api/products/search/{keyword}")
    Call<Response<List<Product>>> searchProducts(@Path("keyword") String keyword);
    
    @Multipart
    @POST("api/products")
    Call<Response<Product>> createProduct(
            @Part("name") RequestBody name,
            @Part("description") RequestBody description,
            @Part("price") RequestBody price,
            @Part("category_id") RequestBody categoryId,
            @Part MultipartBody.Part image
    );
    
    @Multipart
    @PUT("api/products/{id}")
    Call<Response<Product>> updateProduct(
            @Path("id") String id,
            @Part("name") RequestBody name,
            @Part("description") RequestBody description,
            @Part("price") RequestBody price,
            @Part("category_id") RequestBody categoryId,
            @Part MultipartBody.Part image
    );
    
    @DELETE("api/products/{id}")
    Call<Response<Void>> deleteProduct(@Path("id") String id);

    // ==================== ORDER APIs ====================
    @GET("api/orders")
    Call<Response<List<Order>>> getOrders();
    
    @GET("api/orders/user/{userId}")
    Call<Response<List<Order>>> getOrdersByUserId(@Path("userId") String userId);
    
    @GET("api/orders/undelivered")
    Call<Response<List<Order>>> getUndeliveredOrders();
    
    @GET("api/orders/{id}")
    Call<Response<Order>> getOrderById(@Path("id") String id);

    // ==================== STATISTICS APIs ====================
    @GET("api/statistics/products/total")
    Call<Response<Map<String, Integer>>> getTotalProducts();
    
    @GET("api/statistics/products/top5")
    Call<Response<List<Product>>> getTop5Products();
    
    @GET("api/statistics/orders/rate")
    Call<Response<Map<String, Integer>>> getOrderRate();
    
    @GET("api/statistics/revenue/today")
    Call<Response<Map<String, Double>>> getTodayRevenue();
    
    // ==================== DATA VISUALIZATION APIs ====================
    @GET("api/statistics/revenue/last7days")
    Call<Response<List<RevenueData>>> getRevenueLast7Days();
    
    @GET("api/statistics/revenue/last12months")
    Call<Response<List<RevenueData>>> getRevenueLast12Months();
    
    @GET("api/statistics/orders/by-status")
    Call<Response<List<OrderStatusData>>> getOrdersByStatus();
    
    @GET("api/statistics/orders/last7days")
    Call<Response<List<RevenueData>>> getOrdersLast7Days();

    // ==================== CART APIs ====================
    @GET("api/cart/{userId}")
    Call<Response<List<CartItem>>> getCartItems(@Path("userId") String userId);
    
    @POST("api/cart/add")
    Call<Response<CartItem>> addToCart(@Body Map<String, Object> body);
    
    @PUT("api/cart/update/{cartItemId}")
    Call<Response<CartItem>> updateCartItem(@Path("cartItemId") String cartItemId, @Body Map<String, Integer> body);
    
    @DELETE("api/cart/remove/{cartItemId}")
    Call<Response<CartItem>> removeFromCart(@Path("cartItemId") String cartItemId);
    
    @DELETE("api/cart/clear/{userId}")
    Call<Response<Object>> clearCart(@Path("userId") String userId);

    // ==================== CREATE ORDER APIs ====================
    @POST("api/orders/create")
    Call<Response<Order>> createOrder(@Body Map<String, String> body);

    // ==================== CATEGORY APIs ====================
    @GET("api/categories")
    Call<Response<List<DanhMuc>>> getAllCategories();
    
    @POST("api/categories")
    Call<Response<DanhMuc>> createCategory(@Body Map<String, String> body);
    
    @PUT("api/categories/{id}")
    Call<Response<DanhMuc>> updateCategory(@Path("id") String id, @Body Map<String, String> body);
    
    @DELETE("api/categories/{id}")
    Call<Response<Void>> deleteCategory(@Path("id") String id);
}
