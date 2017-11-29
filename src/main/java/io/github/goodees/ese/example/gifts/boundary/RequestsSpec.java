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
package io.github.goodees.ese.example.gifts.boundary;

import io.github.goodees.ese.core.Request;
import org.immutables.value.Value.*;
import io.github.goodees.ese.example.gifts.boundary.ValueObjects.Simple;

/**
 *
 * @author patrik
 */
interface RequestsSpec {

    /**
     * This goes to null entity, and returns a new entity id.
     */
    @Simple
    @Immutable
    interface CreateHousehold extends Request<String> {
    }
    
    @Simple @Immutable
    interface VerifyParent extends Request<Parent> {
        String verificationToken();
    }
    
    @Simple @Immutable
    interface RegisterParent extends Request<VerificationToken> {
        String name();
        String email();
    }
    
    @Simple @Immutable 
    interface RegisterChild extends Request<String> {
        String name();
    }
    
    @Simple @Immutable
    interface RegisterWish extends Request<String> {
        String childId();
        String wishId();
        String content();
    }
    
    @Simple @Immutable
    interface ChangeWish extends Request<Void> {
        String wishId();
        String content();
    }
    
    @Simple @Immutable
    interface DeleteWish extends Request<Void> {
        String wishId();
    }
    
    @Simple @Immutable
    interface Buy extends Request<Void> {
        String wishId();
    }
}
