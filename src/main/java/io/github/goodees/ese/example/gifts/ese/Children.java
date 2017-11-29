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
import io.github.goodees.ese.example.gifts.boundary.Child;
import io.github.goodees.ese.example.gifts.boundary.ChildId;
import io.github.goodees.ese.example.gifts.boundary.Wish;
import io.github.goodees.ese.example.gifts.boundary.WishId;
import io.github.goodees.ese.example.gifts.event.ChildRegisteredEvent;
import io.github.goodees.ese.example.gifts.event.WishContentChangedEvent;
import io.github.goodees.ese.example.gifts.event.WishRegisteredEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author patrik
 */
class Children {
    private TypeSwitch eventHandler = TypeSwitch.builder()
            .on(ChildRegisteredEvent.class, this::onChildRegistered)
            .on(WishRegisteredEvent.class, this::onWishRegistered)
            .on(WishContentChangedEvent.class, this::onWishContentChanged)
            .build();
    
    private Map<ChildId, Child> children = new HashMap<>();
    private Map<ChildId, List<Wish>> wishesOfChild = new HashMap<>();
    private Map<WishId, Wish> wishes = new HashMap<>();
    
    void updateState(Event event) {
        eventHandler.executeMatching(event);
    }
    
    private void onChildRegistered(ChildRegisteredEvent event) {
        ChildId id = ChildId.of(event.id());
        this.children.put(id, Child.of(id, event.name()));
    }
    
    private void onWishRegistered(WishRegisteredEvent event) {
        ChildId childId = ChildId.of(event.childId());
        WishId wishId = WishId.of(event.id());
        List<Wish> wishList = wishesOfChild.computeIfAbsent(childId, (_i) -> new ArrayList<>());
        Wish wish = Wish.of(wishId, childId, event.content());
        wishList.add(wish);
        wishes.put(wishId, wish);
    }
    
    private void onWishContentChanged(WishContentChangedEvent event) {
        Wish oldWish = wishes.get(WishId.of(event.id()));
        Wish newWish = oldWish.withContent(event.newContent());
        wishes.put(oldWish.id(), newWish);
        wishesOfChild.get(oldWish.child()).replaceAll(w -> w == oldWish ? newWish : w);
    }

    boolean exists(ChildId child) {
        return children.containsKey(child);
    }

    boolean hasWish(WishId wish) {
        return wishes.containsKey(wish);
    }

    Map<ChildId, Child> read() {
        return children;
    }

    Iterable<Wish> wishes() {
        return wishes.values();
    }
}
