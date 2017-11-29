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
import io.github.goodees.ese.example.gifts.boundary.Parent;
import io.github.goodees.ese.example.gifts.boundary.ParentId;
import io.github.goodees.ese.example.gifts.event.ParentRegisteredEvent;
import io.github.goodees.ese.example.gifts.event.ParentVerifiedEvent;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author patrik
 */
class Parents {

    private TypeSwitch eventHandler = TypeSwitch.builder()
            .on(ParentRegisteredEvent.class, this::onParentRegistered)
            .on(ParentVerifiedEvent.class, this::onParentVerified)
            .build();
    
    private Map<String, Parent> unverifiedParents = new HashMap<>();
    private Map<ParentId, Parent> parents = new HashMap<>();

    void updateState(Event event) {
        eventHandler.executeMatching(event);
    }
    
    private void onParentRegistered(ParentRegisteredEvent event) {
        unverifiedParents.put(event.id(), Parent.of(ParentId.of(event.id()), event.name(), event.email()));
    }
    
    private void onParentVerified(ParentVerifiedEvent event) {
        Parent parent = unverifiedParents.remove(event.id());
        parents.put(parent.id(), parent);
    }

    Parent verifyParent(String verificationToken) {
        return unverifiedParents.get(verificationToken);
    }

}
