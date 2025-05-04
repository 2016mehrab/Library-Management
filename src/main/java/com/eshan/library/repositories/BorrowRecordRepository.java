package com.eshan.library.repositories;

import com.eshan.library.models.BorrowRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface BorrowRecordRepository extends JpaRepository<BorrowRecord, Integer> {
    @Query("select br from BorrowRecord br where br.returnDate is NULL")
    Page<BorrowRecord> findOngoingBorrowRecords(Pageable pageable);
    @Query("select br from BorrowRecord br where br.returnDate is  not null")
    Page<BorrowRecord> findBorrowHistory(Pageable pageable);
}
