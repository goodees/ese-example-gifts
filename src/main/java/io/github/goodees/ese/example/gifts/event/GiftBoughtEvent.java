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
package io.github.goodees.ese.example.gifts.event;

import io.github.goodees.ese.core.EventSourcedEntity;
import org.immutables.value.Value;

/**
 *
 * @author patrik
 */
@Value.Immutable
public interface GiftBoughtEvent extends GiftEvent {
    String id();
    
    static Builder builder(EventSourcedEntity entity) {
        return new Builder().from(entity);
    }
    
    class Builder extends ImmutableGiftBoughtEvent.Builder implements FromEntity<Builder> {
        
    }
}
