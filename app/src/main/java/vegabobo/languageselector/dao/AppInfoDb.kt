package vegabobo.languageselector.dao

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [AppInfoEntity::class],
    version = 1,
    exportSchema = false // 当前项目未配置 Room schema 导出目录，先关闭导出以消除构建警告。
)
abstract class AppInfoDb : RoomDatabase() {
    abstract fun appInfoDao(): AppInfoDao
}
