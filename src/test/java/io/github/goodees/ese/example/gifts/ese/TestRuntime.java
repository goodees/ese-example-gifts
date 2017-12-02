/*
 * Copyright 2017 patrik.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.goodees.ese.example.gifts.ese;

import io.github.goodees.ese.core.store.EventLog;
import io.github.goodees.ese.core.store.SnapshotStore;
import io.github.goodees.ese.core.sync.ProxiedSyncEventSourcingRuntime;
import io.github.goodees.ese.example.gifts.boundary.WishList;
import io.github.goodees.ese.store.inmemory.InMemoryEventStore;
import io.github.goodees.ese.store.inmemory.InMemorySnapshotStore;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 *
 * @author patrik
 */
public class TestRuntime extends ProxiedSyncEventSourcingRuntime.WithAsyncInterface<WishListEntity, WishList, WishList.Async> {
    InMemoryEventStore eventStore = new InMemoryEventStore();
    InMemorySnapshotStore snapshotStore = new InMemorySnapshotStore();
    ExecutorService executorService = Executors.newCachedThreadPool();
    ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    
    public TestRuntime() {
        super(WishList.class, WishList.Async.class);
    }

    @Override
    protected WishListEntity instantiate(String entityId) {
        return new WishListEntity(entityId, eventStore);
    }

    @Override
    protected void dispose(WishListEntity entity) {
    }

    @Override
    protected SnapshotStore getSnapshotStore() {
        return snapshotStore;
    }

    @Override
    protected EventLog getEventLog() {
        // in-memory store is both a store and a log
        return eventStore;
    }

    @Override
    protected boolean shouldStoreSnapshot(WishListEntity entity, int eventsSinceSnapshot) {
        return false;
    }

    @Override
    protected ExecutorService getExecutorService() {
        return executorService;
    }

    @Override
    protected ScheduledExecutorService getScheduler() {
        return scheduler;
    }

    @Override
    protected String getEntityName() {
        return "WishList";
    }
    
}
