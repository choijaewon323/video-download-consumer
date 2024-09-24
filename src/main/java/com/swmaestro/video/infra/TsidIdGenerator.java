package com.swmaestro.video.infra;

import com.github.f4b6a3.tsid.TsidCreator;
import com.swmaestro.video.domain.IdGenerator;
import org.springframework.stereotype.Component;

@Component
public class TsidIdGenerator implements IdGenerator {
    @Override
    public long makeId() {
        return TsidCreator.getTsid().toLong();
    }
}
