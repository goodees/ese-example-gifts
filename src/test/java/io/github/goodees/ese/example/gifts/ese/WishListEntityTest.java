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

import io.github.goodees.ese.example.gifts.boundary.Child;
import io.github.goodees.ese.example.gifts.boundary.ChildId;
import io.github.goodees.ese.example.gifts.boundary.VerificationToken;
import io.github.goodees.ese.example.gifts.boundary.Wish;
import io.github.goodees.ese.example.gifts.boundary.WishId;
import io.github.goodees.ese.example.gifts.boundary.WishList;
import io.github.goodees.ese.example.gifts.boundary.Wishes;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;

/**
 *
 * @author patrik
 */
public class WishListEntityTest {
    @Rule
    public TestName testName = new TestName();
    
    private TestRuntime runtime = new TestRuntime();
    private WishList entity;
    
    private String name() {
        return testName.getMethodName();
    }
    
    @Before
    public void obtainHandler() {
        entity = runtime.execute(name(), 5, TimeUnit.MILLISECONDS);
    }
    
    @Test
    public void kids_cannot_register_until_parent_verifies() {
        registerShouldFail();
        // so, create first.
        VerificationToken token = entity.create("John Smith", "john@smith.com");
        registerShouldFail();
        entity.verifyParent(token.value());
        ChildId child = entity.registerChild("Johny");
        assertNotNull(child);
    }
    
    @Test
    public void kids_cannot_register_until_parent_verifies_with_event_replay() {
        VerificationToken token = entity.create("John Smith", "john@smith.com");
        entity.verifyParent(token.value());
        // runtime (quite correctly) does not give us any interface to its working memory
        // so we create different runtime with same event store
        TestRuntime otherRuntime = new TestRuntime();
        otherRuntime.eventStore = runtime.eventStore;
        ChildId child = otherRuntime.execute(name()).registerChild("Johny");
        assertNotNull(child);
    }
    
    @Test
    public void registered_wish_can_be_read() {
        VerificationToken token = entity.create("John Smith", "john@smith.com");
        entity.verifyParent(token.value());
        ChildId childId = entity.registerChild("Johny");
        WishId wishId = entity.registerWish(childId, "RC Car");
        Wishes contents = entity.read();
        assertThat(contents.children()).containsOnlyKeys(childId);
        Child child = contents.children().get(childId);
        Wish wish = contents.wishes().get(0);
        assertThat(wish.child()).isEqualTo(childId);
        assertThat(wish.id()).isEqualTo(wishId);
    } 
    
    @Test
    public void registered_wish_can_be_read_async_chain() throws InterruptedException, ExecutionException, TimeoutException {
        WishList.Async async = runtime.executeAsync(name());
        WishId wishId = async.create("John Smith", "john@smith.com")
                .thenCompose((token) -> async.verifyParent(token.value()))
                .thenCompose((p) -> async.registerChild("Johny"))
                .thenCompose((childId) -> async.registerWish(childId, "RC Car"))
                .get(2, TimeUnit.MILLISECONDS);
                
        Wishes contents = entity.read();
        Wish wish = contents.wishes().get(0);
        assertThat(wish.id()).isEqualTo(wishId);
    }     

    private void registerShouldFail() {
        try {
            entity.registerChild("Johny");
            fail("Should have failed");
        } catch (UnsupportedOperationException uoe  ) {
        }
    }
}
