package com.ppsdevelopment.repos;

import com.ppsdevelopment.domain.UsersPref;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserPrefRepo extends JpaRepository<UsersPref,Long> {
    List<UsersPref> getUsersPrefById(long id);
    List<UsersPref> getUsersPrefByIdAndObjectname(long id, String objectName);
    //UsersPref getFirstByUserIdAndObjectname(long id, String objectName);
    UsersPref getFirstByUserIdAndParamName(long id, String objectName);


}
