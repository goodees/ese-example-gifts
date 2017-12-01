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

import io.github.goodees.ese.example.gifts.boundary.ChildId;
import io.github.goodees.ese.example.gifts.boundary.VerificationToken;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
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
    
    private String name() {
        return testName.getMethodName();
    }
    
    @Test
    public void kids_cannot_register_until_parent_verifies() {
        registerShouldFail();
        // so, create first.
        VerificationToken token = runtime.execute(name()).create("John Smith", "john@smith.com");
        registerShouldFail();
        runtime.execute(name()).verifyParent(token.value());
        ChildId child = runtime.execute(name()).registerChild("Johny");
        assertNotNull(child);
    }

    private void registerShouldFail() {
        try {
            runtime.execute(name()).registerChild("Johny");
            fail("Shoudl have failed");
        } catch (UnsupportedOperationException uoe  ) {
            
        }
    }
}
