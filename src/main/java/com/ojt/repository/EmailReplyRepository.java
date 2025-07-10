package com.ojt.repository;

import com.ojt.enumeration.StatusType;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ojt.entity.EmailReply;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmailReplyRepository extends JpaRepository<EmailReply, Long> {

    @Query("""
              SELECT DISTINCT er FROM EmailReply er
              JOIN er.cvList cv
              WHERE er.status.statusType = :statusType
              AND er.subject = :subject
              AND cv.email IN :recipientEmails
          """)
    List<EmailReply> findDuplicateEmails(@Param("statusType") StatusType statusType, @Param("subject") String subject,
                                         @Param("recipientEmails") List<String> recipientEmails);

}
