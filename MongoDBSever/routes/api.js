var express = require('express');
var router = express.Router();
const bcrypt = require('bcryptjs');
const jwt = require('jsonwebtoken');
const { Users, Categories, Products, CartItems, Orders, OrderDetails, Reviews } = require('../models/database'); 

const JWT_SECRET = "NHOM6";
const JWT_EXPIRE = "1h";

// ==================== AUTH APIs ====================
// API register
router.post('/register', async (req, res) => {
  try {
    const { name, email, password, sdt } = req.body;
    console.log("Register body:", req.body);

    // Kiểm tra dữ liệu
    if (!name || !sdt || !email || !password) {
      return res.status(400).json({ success: false, message: "Thiếu dữ liệu khi đăng kí" });
    }

    // Kiểm tra username đã tồn tại chưa
    const existUser = await Users.findOne({ email });
    if (existUser) {
      return res.status(400).json({ success: false, message: "Email đã tồn tại" });
    }

    const newUser = await Users.create({ name, email, password, sdt });

    res.json({
      success: true,
      message: "Đăng ký thành công",
      data: newUser
    });

  } catch (err) {
    res.status(500).json({ success: false, message: err.message });
  }
});

// API login
router.post('/login', async (req, res) => {
  try {
    const { email, password } = req.body;
    if (!email || !password) return res.status(400).json({ success: false, message: "Thiếu dữ liệu" });

    const user = await Users.findOne({ email });
    if (!user) return res.status(400).json({ success: false, message: "Người dùng không tồn tại" });

    if (password !== user.password) {
        return res.status(400).json({ success: false, message: "Sai mật khẩu" });
    }

    const token = jwt.sign({ id: user._id }, JWT_SECRET, { expiresIn: JWT_EXPIRE });
    const refreshToken = jwt.sign({ id: user._id }, JWT_SECRET, { expiresIn: "7d" });

    res.json({
      success: true,
      message: "Đăng nhập thành công",
      token,
      refreshToken,
      data: {
        id: user._id,
        email: user.email,
        name: user.name,
        phone: user.sdt,
        pass: user.password
      }
    });

  } catch (err) {
    res.status(500).json({ success: false, message: err.message });
  }
});

// ==================== PRODUCT APIs ====================
// Lấy tất cả sản phẩm
router.get('/products', async (req, res) => {
  try {
    const products = await Products.find().populate('category_id', 'name');
    res.json({
      success: true,
      message: "Lấy danh sách sản phẩm thành công",
      data: products
    });
  } catch (err) {
    res.status(500).json({ success: false, message: err.message });
  }
});

// Lấy sản phẩm theo ID
router.get('/products/:id', async (req, res) => {
  try {
    const product = await Products.findById(req.params.id).populate('category_id', 'name');
    if (!product) {
      return res.status(404).json({ success: false, message: "Không tìm thấy sản phẩm" });
    }
    res.json({
      success: true,
      message: "Lấy sản phẩm thành công",
      data: product
    });
  } catch (err) {
    res.status(500).json({ success: false, message: err.message });
  }
});

// Tìm kiếm sản phẩm
router.get('/products/search/:keyword', async (req, res) => {
  try {
    const keyword = req.params.keyword;
    const products = await Products.find({
      name: { $regex: keyword, $options: 'i' }
    }).populate('category_id', 'name');
    res.json({
      success: true,
      message: "Tìm kiếm sản phẩm thành công",
      data: products
    });
  } catch (err) {
    res.status(500).json({ success: false, message: err.message });
  }
});

// ==================== ORDER APIs ====================
// Lấy tất cả đơn hàng
router.get('/orders', async (req, res) => {
  try {
    const orders = await Orders.find().populate('user_id', 'name email').sort({ createdAt: -1 });
    const ordersWithDetails = await Promise.all(orders.map(async (order) => {
      const orderDetails = await OrderDetails.find({ order_id: order._id }).populate('product_id', 'name price');
      return {
        ...order.toObject(),
        order_id: `DH${order._id.toString().substring(0, 8)}`,
        details: orderDetails
      };
    }));
    res.json({
      success: true,
      message: "Lấy danh sách đơn hàng thành công",
      data: ordersWithDetails
    });
  } catch (err) {
    res.status(500).json({ success: false, message: err.message });
  }
});

// Lấy đơn hàng theo user_id
router.get('/orders/user/:userId', async (req, res) => {
  try {
    const orders = await Orders.find({ user_id: req.params.userId }).sort({ createdAt: -1 });
    const ordersWithDetails = await Promise.all(orders.map(async (order) => {
      const orderDetails = await OrderDetails.find({ order_id: order._id }).populate('product_id', 'name price');
      return {
        ...order.toObject(),
        order_id: `DH${order._id.toString().substring(0, 8)}`,
        details: orderDetails
      };
    }));
    res.json({
      success: true,
      message: "Lấy danh sách đơn hàng thành công",
      data: ordersWithDetails
    });
  } catch (err) {
    res.status(500).json({ success: false, message: err.message });
  }
});

// Lấy đơn hàng chưa giao (status không phải "Đã giao" hoặc "delivered")
router.get('/orders/undelivered', async (req, res) => {
  try {
    const orders = await Orders.find({
      status: { $nin: ['Đã giao', 'delivered', 'Hủy', 'cancelled'] }
    }).populate('user_id', 'name email').sort({ createdAt: -1 }).limit(5);
    
    const ordersWithDetails = await Promise.all(orders.map(async (order) => {
      const orderDetails = await OrderDetails.find({ order_id: order._id }).populate('product_id', 'name price');
      return {
        ...order.toObject(),
        order_id: `DH${order._id.toString().substring(0, 8)}`,
        details: orderDetails
      };
    }));
    
    res.json({
      success: true,
      message: "Lấy danh sách đơn chưa giao thành công",
      data: ordersWithDetails
    });
  } catch (err) {
    res.status(500).json({ success: false, message: err.message });
  }
});

// Lấy chi tiết đơn hàng
router.get('/orders/:id', async (req, res) => {
  try {
    const order = await Orders.findById(req.params.id).populate('user_id', 'name email phone');
    if (!order) {
      return res.status(404).json({ success: false, message: "Không tìm thấy đơn hàng" });
    }
    const orderDetails = await OrderDetails.find({ order_id: order._id }).populate('product_id');
    res.json({
      success: true,
      message: "Lấy chi tiết đơn hàng thành công",
      data: {
        ...order.toObject(),
        order_id: `DH${order._id.toString().substring(0, 8)}`,
        details: orderDetails
      }
    });
  } catch (err) {
    res.status(500).json({ success: false, message: err.message });
  }
});

// ==================== STATISTICS APIs ====================
// Thống kê tổng sản phẩm
router.get('/statistics/products/total', async (req, res) => {
  try {
    const total = await Products.countDocuments();
    res.json({
      success: true,
      message: "Lấy tổng sản phẩm thành công",
      data: { total }
    });
  } catch (err) {
    res.status(500).json({ success: false, message: err.message });
  }
});

// Top 5 sản phẩm bán chạy
router.get('/statistics/products/top5', async (req, res) => {
  try {
    // Tính tổng số lượng bán của mỗi sản phẩm từ OrderDetails
    const topProducts = await OrderDetails.aggregate([
      {
        $group: {
          _id: '$product_id',
          totalQuantity: { $sum: '$quantity' }
        }
      },
      { $sort: { totalQuantity: -1 } },
      { $limit: 5 },
      {
        $lookup: {
          from: 'products',
          localField: '_id',
          foreignField: '_id',
          as: 'product'
        }
      },
      { $unwind: '$product' }
    ]);
    
    res.json({
      success: true,
      message: "Lấy top 5 sản phẩm bán chạy thành công",
      data: topProducts.map(item => ({
        id: item.product._id,
        name: item.product.name,
        description: item.product.description,
        price: item.product.price,
        image: item.product.image,
        quantity: item.totalQuantity
      }))
    });
  } catch (err) {
    res.status(500).json({ success: false, message: err.message });
  }
});

// Thống kê tỉ lệ đơn hàng
router.get('/statistics/orders/rate', async (req, res) => {
  try {
    const total = await Orders.countDocuments();
    const delivered = await Orders.countDocuments({ status: { $in: ['Đã giao', 'delivered'] } });
    const pending = await Orders.countDocuments({ status: { $in: ['Đang chờ', 'pending'] } });
    const cancelled = await Orders.countDocuments({ status: { $in: ['Hủy', 'cancelled'] } });
    
    res.json({
      success: true,
      message: "Lấy tỉ lệ đơn hàng thành công",
      data: {
        total,
        delivered: total > 0 ? Math.round((delivered / total) * 100) : 0,
        pending: total > 0 ? Math.round((pending / total) * 100) : 0,
        cancelled: total > 0 ? Math.round((cancelled / total) * 100) : 0
      }
    });
  } catch (err) {
    res.status(500).json({ success: false, message: err.message });
  }
});

// Doanh thu hôm nay
router.get('/statistics/revenue/today', async (req, res) => {
  try {
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    const tomorrow = new Date(today);
    tomorrow.setDate(tomorrow.getDate() + 1);
    
    const orders = await Orders.find({
      createdAt: { $gte: today, $lt: tomorrow },
      status: { $in: ['Đã giao', 'delivered'] }
    });
    
    const revenue = orders.reduce((sum, order) => sum + order.total_price, 0);
    
    res.json({
      success: true,
      message: "Lấy doanh thu hôm nay thành công",
      data: { revenue }
    });
  } catch (err) {
    res.status(500).json({ success: false, message: err.message });
  }
});

// Doanh thu 7 ngày gần nhất
router.get('/statistics/revenue/last7days', async (req, res) => {
  try {
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    const sevenDaysAgo = new Date(today);
    sevenDaysAgo.setDate(sevenDaysAgo.getDate() - 7);
    
    const revenueByDay = await Orders.aggregate([
      {
        $match: {
          createdAt: { $gte: sevenDaysAgo, $lte: today },
          status: { $in: ['Đã giao', 'delivered'] }
        }
      },
      {
        $group: {
          _id: { $dateToString: { format: "%Y-%m-%d", date: "$createdAt" } },
          revenue: { $sum: "$total_price" },
          count: { $sum: 1 }
        }
      },
      { $sort: { _id: 1 } }
    ]);
    
    // Tạo mảng đầy đủ 7 ngày (fill missing days với 0)
    const result = [];
    for (let i = 6; i >= 0; i--) {
      const date = new Date(today);
      date.setDate(date.getDate() - i);
      const dateStr = date.toISOString().split('T')[0];
      const found = revenueByDay.find(r => r._id === dateStr);
      result.push({
        date: dateStr,
        revenue: found ? found.revenue : 0,
        count: found ? found.count : 0
      });
    }
    
    res.json({
      success: true,
      message: "Lấy doanh thu 7 ngày gần nhất thành công",
      data: result
    });
  } catch (err) {
    res.status(500).json({ success: false, message: err.message });
  }
});

// Doanh thu 4 tuần gần nhất
router.get('/statistics/revenue/last4weeks', async (req, res) => {
  try {
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    const fourWeeksAgo = new Date(today);
    fourWeeksAgo.setDate(fourWeeksAgo.getDate() - 28);
    
    const revenueByWeek = await Orders.aggregate([
      {
        $match: {
          createdAt: { $gte: fourWeeksAgo, $lte: today },
          status: { $in: ['Đã giao', 'delivered'] }
        }
      },
      {
        $group: {
          _id: {
            year: { $year: "$createdAt" },
            week: { $week: "$createdAt" }
          },
          revenue: { $sum: "$total_price" },
          count: { $sum: 1 }
        }
      },
      { $sort: { "_id.year": 1, "_id.week": 1 } }
    ]);
    
    res.json({
      success: true,
      message: "Lấy doanh thu 4 tuần gần nhất thành công",
      data: revenueByWeek.map(item => ({
        week: `Tuần ${item._id.week}`,
        revenue: item.revenue,
        count: item.count
      }))
    });
  } catch (err) {
    res.status(500).json({ success: false, message: err.message });
  }
});

// Doanh thu 12 tháng gần nhất
router.get('/statistics/revenue/last12months', async (req, res) => {
  try {
    const today = new Date();
    const twelveMonthsAgo = new Date(today);
    twelveMonthsAgo.setMonth(twelveMonthsAgo.getMonth() - 12);
    
    const revenueByMonth = await Orders.aggregate([
      {
        $match: {
          createdAt: { $gte: twelveMonthsAgo, $lte: today },
          status: { $in: ['Đã giao', 'delivered'] }
        }
      },
      {
        $group: {
          _id: { $dateToString: { format: "%Y-%m", date: "$createdAt" } },
          revenue: { $sum: "$total_price" },
          count: { $sum: 1 }
        }
      },
      { $sort: { _id: 1 } }
    ]);
    
    res.json({
      success: true,
      message: "Lấy doanh thu 12 tháng gần nhất thành công",
      data: revenueByMonth.map(item => ({
        month: item._id,
        revenue: item.revenue,
        count: item.count
      }))
    });
  } catch (err) {
    res.status(500).json({ success: false, message: err.message });
  }
});

// Thống kê đơn hàng theo trạng thái (cho Pie Chart)
router.get('/statistics/orders/by-status', async (req, res) => {
  try {
    const statusStats = await Orders.aggregate([
      {
        $group: {
          _id: "$status",
          count: { $sum: 1 },
          totalRevenue: { $sum: "$total_price" }
        }
      }
    ]);
    
    res.json({
      success: true,
      message: "Lấy thống kê đơn hàng theo trạng thái thành công",
      data: statusStats.map(item => ({
        status: item._id || "Chưa xác định",
        count: item.count,
        revenue: item.totalRevenue
      }))
    });
  } catch (err) {
    res.status(500).json({ success: false, message: err.message });
  }
});

// Số lượng đơn hàng theo ngày (7 ngày gần nhất)
router.get('/statistics/orders/last7days', async (req, res) => {
  try {
    const today = new Date();
    today.setHours(0, 0, 0, 0);
    const sevenDaysAgo = new Date(today);
    sevenDaysAgo.setDate(sevenDaysAgo.getDate() - 7);
    
    const ordersByDay = await Orders.aggregate([
      {
        $match: {
          createdAt: { $gte: sevenDaysAgo, $lte: today }
        }
      },
      {
        $group: {
          _id: { $dateToString: { format: "%Y-%m-%d", date: "$createdAt" } },
          count: { $sum: 1 }
        }
      },
      { $sort: { _id: 1 } }
    ]);
    
    // Fill missing days
    const result = [];
    for (let i = 6; i >= 0; i--) {
      const date = new Date(today);
      date.setDate(date.getDate() - i);
      const dateStr = date.toISOString().split('T')[0];
      const found = ordersByDay.find(r => r._id === dateStr);
      result.push({
        date: dateStr,
        count: found ? found.count : 0
      });
    }
    
    res.json({
      success: true,
      message: "Lấy số lượng đơn hàng 7 ngày gần nhất thành công",
      data: result
    });
  } catch (err) {
    res.status(500).json({ success: false, message: err.message });
  }
});

// ==================== CART APIs ====================
// Lấy giỏ hàng của user
router.get('/cart/:userId', async (req, res) => {
  try {
    const cartItems = await CartItems.find({ user_id: req.params.userId })
      .populate('product_id');
    
    // Convert sang plain objects để đảm bảo JSON serialize đúng
    const cartItemsData = cartItems.map(item => item.toObject());
    
    res.json({
      success: true,
      message: "Lấy giỏ hàng thành công",
      data: cartItemsData
    });
  } catch (err) {
    res.status(500).json({ success: false, message: err.message });
  }
});

// Thêm sản phẩm vào giỏ hàng
router.post('/cart/add', async (req, res) => {
  try {
    const { user_id, product_id, quantity } = req.body;
    
    if (!user_id || !product_id || !quantity) {
      return res.status(400).json({ success: false, message: "Thiếu dữ liệu" });
    }

    // Lấy thông tin sản phẩm để tính subtotal
    const product = await Products.findById(product_id);
    if (!product) {
      return res.status(404).json({ success: false, message: "Không tìm thấy sản phẩm" });
    }

    const subtotal = product.price * quantity;

    // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
    const existingItem = await CartItems.findOne({ user_id, product_id });
    
    if (existingItem) {
      // Nếu đã có, cập nhật số lượng
      existingItem.quantity += quantity;
      existingItem.subtotal = existingItem.quantity * product.price;
      await existingItem.save();
      
      // Populate product_id trước khi trả về
      const populatedItem = await CartItems.findById(existingItem._id).populate('product_id');
      
      // Convert sang plain object để đảm bảo JSON serialize đúng
      const cartItemData = populatedItem ? populatedItem.toObject() : existingItem.toObject();
      
      res.json({
        success: true,
        message: "Cập nhật giỏ hàng thành công",
        data: cartItemData
      });
    } else {
      // Nếu chưa có, tạo mới
      const newCartItem = await CartItems.create({
        user_id,
        product_id,
        quantity,
        subtotal
      });
      
      // Populate product_id trước khi trả về
      const populatedItem = await CartItems.findById(newCartItem._id).populate('product_id');
      
      // Convert sang plain object để đảm bảo JSON serialize đúng
      const cartItemData = populatedItem ? populatedItem.toObject() : newCartItem.toObject();
      
      res.json({
        success: true,
        message: "Thêm vào giỏ hàng thành công",
        data: cartItemData
      });
    }
  } catch (err) {
    res.status(500).json({ success: false, message: err.message });
  }
});

// Cập nhật số lượng sản phẩm trong giỏ hàng
router.put('/cart/update/:cartItemId', async (req, res) => {
  try {
    const { quantity } = req.body;
    
    if (!quantity || quantity < 1) {
      return res.status(400).json({ success: false, message: "Số lượng không hợp lệ" });
    }

    const cartItem = await CartItems.findById(req.params.cartItemId)
      .populate('product_id');
    
    if (!cartItem) {
      return res.status(404).json({ success: false, message: "Không tìm thấy sản phẩm trong giỏ hàng" });
    }

    cartItem.quantity = quantity;
    cartItem.subtotal = cartItem.product_id.price * quantity;
    await cartItem.save();

    res.json({
      success: true,
      message: "Cập nhật giỏ hàng thành công",
      data: cartItem
    });
  } catch (err) {
    res.status(500).json({ success: false, message: err.message });
  }
});

// Xóa sản phẩm khỏi giỏ hàng
router.delete('/cart/remove/:cartItemId', async (req, res) => {
  try {
    const cartItem = await CartItems.findByIdAndDelete(req.params.cartItemId);
    
    if (!cartItem) {
      return res.status(404).json({ success: false, message: "Không tìm thấy sản phẩm trong giỏ hàng" });
    }

    res.json({
      success: true,
      message: "Xóa khỏi giỏ hàng thành công",
      data: cartItem
    });
  } catch (err) {
    res.status(500).json({ success: false, message: err.message });
  }
});

// Xóa toàn bộ giỏ hàng của user
router.delete('/cart/clear/:userId', async (req, res) => {
  try {
    await CartItems.deleteMany({ user_id: req.params.userId });
    
    res.json({
      success: true,
      message: "Xóa giỏ hàng thành công"
    });
  } catch (err) {
    res.status(500).json({ success: false, message: err.message });
  }
});

// ==================== ORDER APIs (Đặt hàng từ giỏ) ====================
// Tạo đơn hàng từ giỏ hàng
router.post('/orders/create', async (req, res) => {
  try {
    const { user_id, receiver_name, receiver_address, receiver_phone } = req.body;
    
    if (!user_id || !receiver_name || !receiver_address || !receiver_phone) {
      return res.status(400).json({ success: false, message: "Thiếu thông tin đặt hàng" });
    }

    // Lấy tất cả sản phẩm trong giỏ hàng
    const cartItems = await CartItems.find({ user_id }).populate('product_id');
    
    if (cartItems.length === 0) {
      return res.status(400).json({ success: false, message: "Giỏ hàng trống" });
    }

    // Tính tổng tiền
    const total_price = cartItems.reduce((sum, item) => sum + item.subtotal, 0);

    // Tạo đơn hàng
    const order = await Orders.create({
      user_id,
      total_price,
      status: "Đang chờ",
      receiver_name,
      receiver_address,
      receiver_phone
    });

    // Tạo chi tiết đơn hàng từ giỏ hàng
    const orderDetails = await Promise.all(cartItems.map(item => {
      return OrderDetails.create({
        order_id: order._id,
        product_id: item.product_id._id,
        quantity: item.quantity,
        price: item.product_id.price,
        subtotal: item.subtotal
      });
    }));

    // Xóa giỏ hàng sau khi đặt hàng thành công
    await CartItems.deleteMany({ user_id });

    res.json({
      success: true,
      message: "Đặt hàng thành công",
      data: {
        ...order.toObject(),
        order_id: `DH${order._id.toString().substring(0, 8)}`,
        details: orderDetails
      }
    });
  } catch (err) {
    res.status(500).json({ success: false, message: err.message });
  }
});

module.exports = router;
