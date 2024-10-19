package com.zuofw.domain.repository;


import com.zuofw.domain.entity.NoteContentDO;
import org.springframework.data.cassandra.repository.CassandraRepository;

import java.util.UUID;

/**
 * @author: zuofw
 */

public interface NoteContentRepository extends CassandraRepository<NoteContentDO, UUID> {

}

