#!/bin/bash

# 数据库快速恢复脚本
# 使用方法: ./quick_restore.sh

echo "🔄 开始数据库恢复过程..."
echo "⚠️  警告: 此操作将覆盖现有数据库!"

# 数据库配置
DB_USER="root"
DB_PASS="qichengxu123"
DB_NAME="vliascrm"
BACKUP_FILE="vliascrm_backup_20250805_221518.sql"

# 检查备份文件是否存在
if [ ! -f "$BACKUP_FILE" ]; then
    echo "❌ 错误: 备份文件 $BACKUP_FILE 不存在!"
    exit 1
fi

# 提示用户确认
read -p "确定要恢复数据库吗? (输入 'YES' 继续): " confirm
if [ "$confirm" != "YES" ]; then
    echo "❌ 操作已取消"
    exit 0
fi

echo "📋 恢复步骤:"
echo "1. 删除现有数据库..."
mysql -u $DB_USER -p$DB_PASS -e "DROP DATABASE IF EXISTS $DB_NAME;" 2>/dev/null

echo "2. 创建新数据库..."
mysql -u $DB_USER -p$DB_PASS -e "CREATE DATABASE $DB_NAME CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;" 2>/dev/null

echo "3. 恢复数据..."
mysql -u $DB_USER -p$DB_PASS $DB_NAME < $BACKUP_FILE 2>/dev/null

# 验证恢复结果
echo "4. 验证恢复结果..."
TABLE_COUNT=$(mysql -u $DB_USER -p$DB_PASS $DB_NAME -e "SHOW TABLES;" 2>/dev/null | wc -l)
TABLE_COUNT=$((TABLE_COUNT - 1))  # 减去表头

if [ $TABLE_COUNT -gt 0 ]; then
    echo "✅ 数据库恢复成功!"
    echo "📊 恢复了 $TABLE_COUNT 个表"
    
    echo ""
    echo "📋 表统计信息:"
    mysql -u $DB_USER -p$DB_PASS $DB_NAME -e "
        SELECT 
            TABLE_NAME as '表名',
            TABLE_ROWS as '记录数'
        FROM information_schema.TABLES 
        WHERE TABLE_SCHEMA = '$DB_NAME' 
        ORDER BY TABLE_ROWS DESC
        LIMIT 10;
    " 2>/dev/null
else
    echo "❌ 数据库恢复失败!"
    exit 1
fi

echo ""
echo "🎉 恢复完成! 现在可以重启应用程序了." 