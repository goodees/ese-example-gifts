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

import io.github.goodees.ese.core.Event;
import io.github.goodees.ese.core.matching.TypeSwitch;
import io.github.goodees.ese.core.store.EventStore;
import io.github.goodees.ese.core.sync.ProxiedSyncEntity;
import io.github.goodees.ese.example.gifts.boundary.ChildId;
import io.github.goodees.ese.example.gifts.boundary.Parent;
import io.github.goodees.ese.example.gifts.boundary.VerificationToken;
import io.github.goodees.ese.example.gifts.boundary.WishId;
import io.github.goodees.ese.example.gifts.boundary.WishList;
import io.github.goodees.ese.example.gifts.boundary.Wishes;
import io.github.goodees.ese.example.gifts.event.*;
import java.util.UUID;

/**
 *
 * @author patrik
 */
public class WishListEntity extends ProxiedSyncEntity<WishList> {

    public WishListEntity(String id, EventStore store) {
        super(id, store);
    }        
    
    private final WishList initialRequestHandler = new WishList() {
        @Override
        public VerificationToken create(String firstParentName, String firstParentEmail) {
            return WishListEntity.this.create(firstParentName, firstParentEmail);
        }

        @Override
        public Parent verifyParent(String verificationToken) {
            throw new UnsupportedOperationException("Not created");
        }

        @Override
        public VerificationToken registerParent(String name, String email) {
            throw new UnsupportedOperationException("Not created");
        }

        @Override
        public ChildId registerChild(String name) {
            throw new UnsupportedOperationException("Not created");
        }

        @Override
        public WishId registerWish(ChildId child, String content) {
            throw new UnsupportedOperationException("Not supported yet."); 
        }

        @Override
        public void changeWish(WishId wish, String content) {
            throw new UnsupportedOperationException("Not supported yet."); 
        }

        @Override
        public Wishes read() {
            throw new UnsupportedOperationException("Not supported yet."); 
        }
    };
    private final WishList parentRegistrationRequestHandler = new WishList() {
        @Override
        public VerificationToken create(String firstParentName, String firstParentEmail) {
            throw new UnsupportedOperationException("Already created");
        }

        @Override
        public Parent verifyParent(String verificationToken) {
            return WishListEntity.this.verifyParent(verificationToken);
        }

        @Override
        public VerificationToken registerParent(String name, String email) {
            return WishListEntity.this.registerParent(name, email);
        }

        @Override
        public ChildId registerChild(String name) {
            throw new UnsupportedOperationException("Not yet registered");
        }

        @Override
        public WishId registerWish(ChildId child, String content) {
            throw new UnsupportedOperationException("Not yet registered");
        }

        @Override
        public void changeWish(WishId wish, String content) {
            throw new UnsupportedOperationException("Not yet registered");
        }

        @Override
        public Wishes read() {
            throw new UnsupportedOperationException("Not yet registered");
        }
        
    };
    private final WishList openRequestHandler = new WishList() {
        @Override
        public VerificationToken create(String firstParentName, String firstParentEmail) {
            throw new UnsupportedOperationException("Wishlist already exists");
        }

        @Override
        public Parent verifyParent(String verificationToken) {
            return WishListEntity.this.verifyParent(verificationToken);
        }

        @Override
        public VerificationToken registerParent(String name, String email) {
            return WishListEntity.this.registerParent(name, email);
        }

        @Override
        public ChildId registerChild(String name) {
            return WishListEntity.this.registerChild(name);
        }

        @Override
        public WishId registerWish(ChildId child, String content) {
            return WishListEntity.this.registerWish(child, content);
        }

        @Override
        public void changeWish(WishId wish, String content) {
            WishListEntity.this.changeWish(wish, content);
        }

        @Override
        public Wishes read() {
            return WishListEntity.this.read();
        }
    };
    
    private WishList requestHandler = initialRequestHandler;
    @Override
    protected WishList requestHandler() {
        return this.requestHandler;
    }

    private VerificationToken create(String firstParentName, String firstParentEmail) {
        if (getStateVersion() != 0) {
            throw new IllegalStateException("Wishlist already exists and cannot be created");
        } else {
            return registerParent(firstParentName, firstParentEmail);
        }
    }

    private VerificationToken registerParent(String parentName, String parentEmail) {
        String id = UUID.randomUUID().toString();
        persistAndUpdate(ParentRegisteredEvent.builder(this).name(parentName).email(parentEmail).id(id).build());
        return VerificationToken.of(id);
    }

    @Override
    protected void updateState(Event event) {
        if (getStateVersion() == 0) {
            // after first event we wait for registration
            requestHandler = parentRegistrationRequestHandler;
        }
        if (event instanceof ParentVerifiedEvent) {
            // after first parent registers we offer all functionality;
            requestHandler = openRequestHandler;
        }
        parents.updateState(event);
        children.updateState(event);
    }
    
    private Parents parents = new Parents();
    private Children children = new Children();

    private Parent verifyParent(String verificationToken) {
        Parent parent = parents.verifyParent(verificationToken);
        if (parent != null) {
            persistAndUpdate(ParentVerifiedEvent.builder(this).id(parent.id().value()).build());
            return parent;
        } else {
            throw new IllegalArgumentException("No such parent pending registration");
        }
    }

    private ChildId registerChild(String name) {
        String id = UUID.randomUUID().toString();
        persistAndUpdate(ChildRegisteredEvent.builder(this).id(id).name(name).build());
        return ChildId.of(id);
    }

    private WishId registerWish(ChildId child, String content) {
        String id = UUID.randomUUID().toString();
        if (!children.exists(child)) {
            throw new IllegalArgumentException("Says who?");
        }
        persistAndUpdate(WishRegisteredEvent.builder(this).id(id).childId(child.value()).content(content).build());
        return WishId.of(id);
    }

    private void changeWish(WishId wish, String content) {
        if (!children.hasWish(wish)) {
            throw new IllegalArgumentException("No such wish");
        }
        persistAndUpdate(WishContentChangedEvent.builder(this).id(wish.value()).newContent(content).build());
    }

    private Wishes read() {
        return Wishes.builder()
                .children(children.read())
                .addAllWishes(children.wishes())
                .build();
    }
    
}
