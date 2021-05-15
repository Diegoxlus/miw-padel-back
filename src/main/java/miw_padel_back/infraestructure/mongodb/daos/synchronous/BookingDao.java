package miw_padel_back.infraestructure.mongodb.daos.synchronous;

import miw_padel_back.infraestructure.mongodb.entities.BookingEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookingDao extends MongoRepository<BookingEntity,String> {
}
