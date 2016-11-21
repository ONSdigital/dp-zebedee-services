package userservice.repositories;


import org.springframework.data.repository.CrudRepository;
import userservice.entities.UserDetails;

import java.util.List;

public interface UserDetailsRepository extends CrudRepository<UserDetails, Long> {

    List<UserDetails> findByEmail(final String email);
}
