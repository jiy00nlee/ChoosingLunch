package com.example.toyproject_server.PlaceDatabase

import org.springframework.data.jpa.repository.JpaRepository

interface JpaPlaceRepository : JpaRepository<Place,String>, PlaceRepository{ //String은 'Place'의 id(pk).
    //커스텀할 DB function만

/*
    @Query(value= "SELECT *, (6371*acos(cos(radians(:lat))*cos(radians(Y))*cos(radians(X)-radians(:lng))" +
            "+ sin(radians(:lat))*sin(radians(Y))) AS distance FROM PLACE HAVING distance <=1 ORDER BY distance" , nativeQuery = true) */
    //이거 사실 필요없는 듯?

    override fun findByCategoryname(category: String) : List<Place>

}
