/*
 * Copyright (c) 2013, 2019, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package jdk.graal.compiler.lir.amd64.vector;

import static jdk.vm.ci.code.ValueUtil.asRegister;
import static jdk.vm.ci.code.ValueUtil.isRegister;

import jdk.graal.compiler.asm.amd64.AMD64Address;
import jdk.graal.compiler.asm.amd64.AMD64Assembler.VexRMOp;
import jdk.graal.compiler.asm.amd64.AMD64MacroAssembler;
import jdk.graal.compiler.asm.amd64.AVXKind.AVXSize;
import jdk.graal.compiler.lir.LIRInstructionClass;
import jdk.graal.compiler.lir.Opcode;
import jdk.graal.compiler.lir.asm.CompilationResultBuilder;

import jdk.vm.ci.meta.AllocatableValue;

public final class AMD64VectorCompareOp extends AMD64VectorInstruction {
    public static final LIRInstructionClass<AMD64VectorCompareOp> TYPE = LIRInstructionClass.create(AMD64VectorCompareOp.class);

    @Opcode private final VexRMOp opcode;
    @Use({OperandFlag.REG}) protected AllocatableValue x;
    @Use({OperandFlag.REG, OperandFlag.STACK}) protected AllocatableValue y;

    public AMD64VectorCompareOp(VexRMOp opcode, AllocatableValue x, AllocatableValue y) {
        this(opcode, AVXSize.XMM, x, y);
    }

    public AMD64VectorCompareOp(VexRMOp opcode, AVXSize size, AllocatableValue x, AllocatableValue y) {
        super(TYPE, size);
        this.opcode = opcode;
        this.x = x;
        this.y = y;
    }

    @Override
    public void emitCode(CompilationResultBuilder crb, AMD64MacroAssembler masm) {
        if (isRegister(y)) {
            opcode.emit(masm, size, asRegister(x), asRegister(y));
        } else {
            opcode.emit(masm, size, asRegister(x), (AMD64Address) crb.asAddress(y));
        }
    }
}
