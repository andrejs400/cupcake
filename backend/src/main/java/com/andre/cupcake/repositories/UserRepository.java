package com.andre.cupcake.repositories;

import com.andre.cupcake.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	User findByEmail(String email);
	
	@Query("SELECT DISTINCT obj FROM User obj WHERE "
			+ "(LOWER(obj.firstName) LIKE LOWER(CONCAT('%',:name,'%')) )")
	Page<User> find(String name, Pageable pageable);
}
