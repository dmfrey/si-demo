package io.pivotal.dmfrey.sidemo.persistence;

import org.springframework.data.repository.CrudRepository;

public interface DataRecordRepository extends CrudRepository<DataRecord, Long> {
}
