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

import org.immutables.value.Value.*;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.List;
import java.util.Map;
import org.immutables.value.Value;

/**
 *
 * @author patrik
 */
interface ValueObjects {
    @Retention(RetentionPolicy.SOURCE)
    @Value.Style(allMandatoryParameters = true, typeImmutable = "*")
    @interface Simple {
        
    }
    @Simple @Immutable
    interface VerificationToken {
        String value();
    }

    @Simple @Immutable
    interface Parent {
        ParentId id();
        String name();
        String email();
    }  
    
    @Simple @Immutable
    interface ChildId {
        String value();
    }
    
    @Simple @Immutable
    interface WishId {
        String value();
    }
    
    @Simple @Immutable
    interface ParentId {
        String value();
    }
    
    @Simple @Immutable
    interface Child {
        ChildId id();
        String name();
    }
    
    @Simple @Immutable
    interface Wish {
        ChildId child();
        String content();
        boolean fulfilled();
    }
    
    @Simple @Immutable
    interface Wishes {
        Map<ChildId, Child> children();
        List<Wish> wishes();
    }
}
