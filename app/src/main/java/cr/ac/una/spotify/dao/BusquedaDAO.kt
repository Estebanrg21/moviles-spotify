package cr.ac.una.roomdb

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import cr.ac.una.spotify.entity.Busqueda


@Dao
interface BusquedaDAO {
    @Insert
    fun insert(entity: Busqueda)

    @Query("SELECT * FROM busqueda WHERE texto LIKE '%' || :searchString || '%' GROUP BY texto")
    fun getBusquedas(searchString: String): List<Busqueda?>?
}