package com.raghu.adslite.objects;

import java.util.concurrent.atomic.AtomicLong;

// Enable javadocs
public class AdImpression {

    private long id;

    private AtomicLong count;

    private AdImpression(long id, AtomicLong count) {
        this.id = id;
        this.count = count;
    }
    public long getId() {
        return id;
    }

    public AtomicLong getCount() {
        return count;
    }

    public static class Builder {
        private long id;
        private AtomicLong count;
        public Builder(long id, AtomicLong count) {
            this.id = id;
            this.count = count;
        }
        public AdImpression build() {
            return new AdImpression(id, count);
        }
    }
}
