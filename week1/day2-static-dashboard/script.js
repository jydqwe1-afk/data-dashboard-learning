// 数据看板静态页面 - 交互脚本
// 第1周第2天实战编码

// ========== 常量定义 ==========
const REFRESH_INTERVAL = 10000; // 数据刷新间隔（毫秒）
const TOAST_DURATION = 3000; // Toast 显示时长（毫秒）

// ========== 页面加载完成后执行 ==========
document.addEventListener("DOMContentLoaded", function () {
  console.log("数据看板页面初始化...");

  // 1. 更新当前日期和时间
  updateCurrentDateTime();

  // 2. 为指标卡片添加点击事件
  setupCardInteractions();

  // 3. 为图表柱状图添加交互
  setupChartInteractions();

  // 4. 设置按钮事件
  setupButtonEvents();

  // 5. 模拟数据自动更新（每10秒）
  setInterval(updateMetrics, REFRESH_INTERVAL);

  // 6. 初始化主题（检查本地存储）
  initTheme();
});

// 更新当前日期和时间
function updateCurrentDateTime() {
  const now = new Date();

  // 格式化日期
  const dateOptions = { year: "numeric", month: "long", day: "numeric" };
  const dateString = now.toLocaleDateString("zh-CN", dateOptions);
  document.getElementById("current-date").textContent = dateString;

  // 格式化时间（用于最后更新时间）
  const timeOptions = { hour: "2-digit", minute: "2-digit" };
  const timeString = now.toLocaleTimeString("zh-CN", timeOptions);
  document.getElementById("last-updated").textContent =
    `${dateString} ${timeString}`;
}

// 设置卡片交互
function setupCardInteractions() {
  const cards = document.querySelectorAll(".metric-card");

  cards.forEach((card) => {
    // 点击卡片切换激活状态
    card.addEventListener("click", function () {
      // 移除其他卡片的激活状态
      cards.forEach((c) => c.classList.remove("active"));
      // 添加当前卡片的激活状态
      this.classList.add("active");

      // 显示卡片详细信息
      const cardTitle = this.querySelector("h3").textContent;
      const cardValue = this.querySelector(".metric-value").textContent;
      console.log(`选中卡片: ${cardTitle}, 当前值: ${cardValue}`);

      // 可选：显示提示信息（生产环境中可使用更优雅的UI）
      showToast(`已选中 ${cardTitle}`, "info");
    });

    // 键盘导航支持
    card.setAttribute("tabindex", "0");
    card.addEventListener("keypress", function (e) {
      if (e.key === "Enter" || e.key === " ") {
        e.preventDefault();
        this.click();
      }
    });
  });
}

// 设置图表交互
function setupChartInteractions() {
  const bars = document.querySelectorAll(".chart-bar");

  bars.forEach((bar) => {
    // 鼠标悬停时高亮显示
    bar.addEventListener("mouseenter", function () {
      this.style.opacity = "0.8";
      this.style.filter = "brightness(1.2)";

      // 显示数据提示
      const month = this.getAttribute("data-month");
      const value = this.getAttribute("data-value");
      console.log(`${month}销售额: ${value}万元`);
    });

    bar.addEventListener("mouseleave", function () {
      this.style.opacity = "1";
      this.style.filter = "brightness(1)";
    });

    // 点击柱状图显示详细信息
    bar.addEventListener("click", function () {
      const month = this.getAttribute("data-month");
      const value = this.getAttribute("data-value");
      showToast(`${month}销售额: ${value}万元`, "success");
    });
  });
}

// 设置按钮事件
function setupButtonEvents() {
  const refreshBtn = document.getElementById("refresh-btn");
  const themeToggle = document.getElementById("theme-toggle");

  // 刷新数据按钮
  if (refreshBtn) {
    refreshBtn.addEventListener("click", function () {
      console.log("手动刷新数据...");
      updateMetrics();
      showToast("数据已刷新", "success");

      // 添加点击反馈
      this.classList.add("clicked");
      setTimeout(() => {
        this.classList.remove("clicked");
      }, 300);
    });
  }

  // 主题切换按钮
  if (themeToggle) {
    themeToggle.addEventListener("click", function () {
      toggleTheme();
    });
  }

  // 键盘快捷键
  document.addEventListener("keydown", function (e) {
    // R键刷新数据
    if (e.key === "r" || e.key === "R") {
      if (!e.ctrlKey && !e.metaKey) {
        e.preventDefault();
        refreshBtn.click();
      }
    }
    // T键切换主题
    if (e.key === "t" || e.key === "T") {
      if (!e.ctrlKey && !e.metaKey) {
        e.preventDefault();
        themeToggle.click();
      }
    }
  });
}

// 模拟数据更新
function updateMetrics() {
  console.log("更新指标数据...");

  // 模拟数据变化函数
  const randomChange = (base, range) => {
    const change = Math.random() * range * 2 - range;
    return base * (1 + change / 100);
  };

  // 更新销售额（在基础值125000上下浮动5%）
  const newSales = randomChange(125000, 5);
  document.getElementById("sales-value").textContent =
    `¥${Math.round(newSales).toLocaleString()}`;

  // 更新订单数（在基础值1248上下浮动3%）
  const newOrders = randomChange(1248, 3);
  document.getElementById("orders-value").textContent =
    Math.round(newOrders).toLocaleString();

  // 更新用户增长（在基础值342上下浮动10%）
  const newUsers = randomChange(342, 10);
  document.getElementById("users-value").textContent =
    `+${Math.round(newUsers)}`;

  // 更新转化率（在基础值4.8上下浮动0.3%）
  const newConversion = randomChange(4.8, 0.3);
  document.getElementById("conversion-value").textContent =
    `${newConversion.toFixed(1)}%`;

  // 更新最后更新时间
  updateCurrentDateTime();

  // 随机更新一个柱状图高度（模拟动态数据）
  updateRandomChartBar();
}

// 随机更新一个柱状图高度
function updateRandomChartBar() {
  const bars = document.querySelectorAll(".chart-bar");
  if (bars.length === 0) return;

  const randomIndex = Math.floor(Math.random() * bars.length);
  const bar = bars[randomIndex];
  const currentValue = parseInt(bar.getAttribute("data-value"));

  // 随机变化 ±10
  const change = Math.floor(Math.random() * 20) - 10;
  const newValue = Math.max(30, Math.min(100, currentValue + change));

  // 更新数据属性和高度
  bar.setAttribute("data-value", newValue);
  bar.style.height = `${newValue}%`;

  console.log(
    `更新图表柱状图: ${bar.getAttribute("data-month")} 从 ${currentValue} 到 ${newValue}`,
  );
}

// 主题功能
function initTheme() {
  const savedTheme = localStorage.getItem("dashboard-theme");
  if (savedTheme === "dark") {
    document.body.classList.add("dark-theme");
    updateThemeButtonIcon(true);
  }
}

function toggleTheme() {
  const isDark = document.body.classList.toggle("dark-theme");

  // 保存到本地存储
  localStorage.setItem("dashboard-theme", isDark ? "dark" : "light");

  // 更新按钮图标
  updateThemeButtonIcon(isDark);

  // 显示提示
  showToast(`已切换到${isDark ? "深色" : "浅色"}主题`, "info");
}

function updateThemeButtonIcon(isDark) {
  const themeToggle = document.getElementById("theme-toggle");
  if (!themeToggle) return;

  const icon = themeToggle.querySelector("i");
  if (icon) {
    icon.className = isDark ? "fas fa-sun" : "fas fa-moon";
  }

  // 更新文字（保留 i 标签，只更新文本节点）
  const textNode = themeToggle.childNodes[themeToggle.childNodes.length - 1];
  if (textNode && textNode.nodeType === Node.TEXT_NODE) {
    textNode.textContent = ` ${isDark ? "浅色主题" : "深色主题"}`;
  }
}

// 工具函数：显示提示信息
function showToast(message, type = "info") {
  // 创建提示元素
  const toast = document.createElement("div");
  toast.className = `toast toast-${type}`;
  toast.textContent = message;

  // 添加到页面
  document.body.appendChild(toast);

  // 触发重排以启动动画
  toast.offsetHeight;

  // 淡入
  requestAnimationFrame(() => {
    toast.style.opacity = "1";
  });

  // 3秒后淡出并移除
  setTimeout(() => {
    toast.style.opacity = "0";
    setTimeout(() => {
      if (toast.parentNode) {
        document.body.removeChild(toast);
      }
    }, 300);
  }, TOAST_DURATION);
}

// 错误处理
window.addEventListener("error", function (e) {
  console.error("JavaScript错误:", e.message);
  showToast(`脚本错误: ${e.message}`, "error");
});

// 导出函数（供控制台调试）
window.dashboard = {
  updateMetrics,
  toggleTheme,
  showToast,
};
