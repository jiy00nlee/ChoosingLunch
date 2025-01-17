package com.example.toyproject_client.data


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.toyproject_client.data.MenuInfoData.MenuInfoDao
import com.example.toyproject_client.data.MenuInfoData.MenuInfoEntity
import com.example.toyproject_client.data.PaymentData.DateTypeConverter
import com.example.toyproject_client.data.PaymentData.PaymentDao
import com.example.toyproject_client.data.PaymentData.PaymentEntity
import com.example.toyproject_client.data.favoriteStoreData.FavoriteStoreDao
import com.example.toyproject_client.data.favoriteStoreData.FavoriteStoreEntity
import com.example.toyproject_client.data.UserData.UserDataDao
import com.example.toyproject_client.data.UserData.UserDataEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Database(entities = arrayOf(UserDataEntity::class, FavoriteStoreEntity::class, MenuInfoEntity::class, PaymentEntity::class), version = 7, exportSchema = false)
@TypeConverters(DateTypeConverter::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun UserDataDao(): UserDataDao
    abstract fun FavoriteStoreDao() : FavoriteStoreDao
    abstract fun MenuInfoDao() : MenuInfoDao
    abstract fun PaymentDao() : PaymentDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .addCallback(AppDatabaseCallback(scope))
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

    private class AppDatabaseCallback(      //이전에 만들어진 정보가 있는 경우 (null이 아닌경우), 그 정보를 가져온다.
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) { //데이터베이스 공간에 데이터를 처음 넣어줌.
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    UserDatabase(database.UserDataDao())
                    FavoriteStoreDatabase(database.FavoriteStoreDao())
                    MenuInfoDatabase(database.MenuInfoDao())
                    PaymentDatabase(database.PaymentDao())
                }
            }
        }

        suspend fun UserDatabase(userdataDao: UserDataDao) {
            //LocationDao.insert(LocationEntity("Lilly","여",1)) //초기 데이터 넣어주기.(ex.서버) -> 이번어플에서는 X.
        }
        suspend fun FavoriteStoreDatabase(favoritestoreDao : FavoriteStoreDao){ }
        suspend fun MenuInfoDatabase(menuInfoDao: MenuInfoDao) {}
        suspend fun PaymentDatabase(paymentDao : PaymentDao ) {}
    }

}