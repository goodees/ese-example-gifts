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

import java.util.concurrent.CompletableFuture;

/**
 *
 * @author patrik
 */
public interface WishList {
    VerificationToken create(String firstParentName, String firstParentEmail);
    Parent verifyParent(String verificationToken);
    VerificationToken registerParent(String name, String email);
    ChildId registerChild(String name);
    WishId registerWish(ChildId child, String content);
    void changeWish(WishId wish, String content);
    Wishes read();
    
    interface Async {
        CompletableFuture<VerificationToken> create(String firstParentName, String firstParentEmail);
        CompletableFuture<Parent> verifyParent(String verificationToken);
        CompletableFuture<VerificationToken> registerParent(String name, String email);
        CompletableFuture<ChildId> registerChild(String name);
        CompletableFuture<WishId> registerWish(ChildId child, String content);
        CompletableFuture<Void> changeWish(WishId wish, String content);
        CompletableFuture<Wishes> read();
    }
}
