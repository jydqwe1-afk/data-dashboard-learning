# 数据看板销售指标变量定义
total_sales = 125000.50  # 浮点数：总销售额
monthly_growth = 12.5    # 整数：月增长率（%）
company_name = "数据科技公司"  # 字符串：公司名称
is_premium_user = True   # 布尔值：是否高级用户

# 类型查看与转换
print(type(total_sales))  # <class 'float'>
growth_str = str(monthly_growth) + "%"  # 转换为字符串

# 练习：定义订单数量、用户增长数、转化率变量

# 基于销售数据的条件判断
sales_target = 100000
actual_sales = 125000

if actual_sales >= sales_target:
    bonus = 5000
    print(f"达成销售目标！奖金：¥{bonus}")
elif actual_sales >= sales_target * 0.8:
    print("接近目标，继续努力")
else:
    print("未达成目标，需分析原因")

# 循环处理月度销售数据
monthly_sales = [85000, 92000, 105000, 125000, 118000]
total = 0
for sales in monthly_sales:
    total += sales
    print(f"月度销售额：¥{sales:,.2f}")

print(f"季度总销售额：¥{total:,.2f}")

# 练习：计算平均销售额，找出最高/最低月份
