package cr.ac.una.spotify.db

import android.content.Context
import androidx.room.*
import cr.ac.una.roomdb.BusquedaDAO
import cr.ac.una.roomdb.converter.Converters
import cr.ac.una.spotify.entity.Busqueda

@Database(entities = [Busqueda::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase  : RoomDatabase() {
    abstract fun busquedaDao(): BusquedaDAO
    companion object {
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                synchronized(AppDatabase::class) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "spotify-database"
                    ).build()
                }
            }
            return instance!!
        }
    }
}