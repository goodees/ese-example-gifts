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
class ValueObjects {

    @Immutable
    @Prefixed
    interface _VerificationToken {
        String value();
    }

    @Immutable
    @Prefixed
    interface _Parent {
        ParentId id();
        String name();
        String email();
    }

    @Immutable
    @Prefixed
    interface _Child {
        ChildId id();
        String name();
    }

    @Immutable
    @Prefixed
    interface _Wish {
        WishId id();
        ChildId child();
        String content();

        @Default
        default boolean fulfilled() {
            return false;
        }
    }

    @Immutable(builder = true)
    @Prefixed
    interface _Wishes {
        Map<ChildId, Child> children();
        List<Wish> wishes();
    }

    @Immutable
    @Prefixed
    interface _ChildId {
        String value();
    }

    @Immutable
    @Prefixed
    interface _WishId {
        String value();
    }

    @Immutable
    @Prefixed
    interface _ParentId {
        String value();
    }

    @Retention(RetentionPolicy.SOURCE)
    @Value.Style(allMandatoryParameters = true, typeAbstract = "_*",
        // Generate without any suffix, just raw detected name
        typeImmutable = "*", visibility = Style.ImplementationVisibility.PUBLIC,
        defaults = @Immutable(builder = false))
    @interface Prefixed {

    }
}
