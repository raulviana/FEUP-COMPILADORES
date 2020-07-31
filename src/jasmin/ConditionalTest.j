.class public ConditionalTest
.super java/lang/Object
.field private booleanVar Z

.method public <init>()V
	aload_0
	invokenonvirtual java/lang/Object/<init>()V
	return
.end method

.method public setBooleanVar(Z)Z
	.limit stack 2
	.limit locals 2

	aload_0
	iload_1
	putfield ConditionalTest/booleanVar Z

	iconst_1
	ireturn
.end method


.method public getBooleanVar()Z
	.limit stack 2
	.limit locals 1

	aload_0
	getfield ConditionalTest/booleanVar Z
	ireturn
.end method


.method public static main([Ljava/lang/String;)V
	.limit stack 2
	.limit locals 7

	new ConditionalTest
	dup
	invokespecial ConditionalTest/<init>()V
	astore_1

	aload_1
	iconst_1
	invokevirtual ConditionalTest/setBooleanVar(Z)Z

	pop
	iconst_0
	istore_2

while_1_begin:
	aload_1
	invokevirtual ConditionalTest/getBooleanVar()Z

	ifeq while_1_end
	aload_1
	iconst_0
	invokevirtual ConditionalTest/setBooleanVar(Z)Z

	pop
	iconst_1
	istore_2

	goto while_1_begin
while_1_end:
	iconst_1
	iload_2
	invokestatic PrintTest/printTest(IZ)V

	aload_1
	invokevirtual ConditionalTest/getBooleanVar()Z

	ifne if_2_else
	iconst_1
	istore_3

	goto if_2_end
if_2_else:
	iconst_0
	istore_3

if_2_end:
	iconst_2
	iload_3
	invokestatic PrintTest/printTest(IZ)V

	iload_3
	ifeq if_3_else
	iconst_1
	istore 4

	goto if_3_end
if_3_else:
	iconst_0
	istore 4

if_3_end:
	iconst_3
	iload 4
	invokestatic PrintTest/printTest(IZ)V

	iload_2
	ifeq if_4_else
	iload_3
	ifeq if_5_else
	iload 4
	ifeq if_6_else
	iconst_1
	istore 5

	goto if_6_end
if_6_else:
	iconst_0
	istore 5

if_6_end:
	goto if_5_end
if_5_else:
	iconst_0
	istore 5

if_5_end:
	goto if_4_end
if_4_else:
	iconst_0
	istore 5

if_4_end:
	iconst_4
	iload 5
	invokestatic PrintTest/printTest(IZ)V

	iload_2
	ifeq AND_8
	iload_3
	ifeq AND_8
	iconst_1
	goto AND_8_end
AND_8:
	iconst_0
AND_8_end:
	ifeq if_7_else
	iload 4
	ifeq if_7_else
	iconst_1
	istore 6

	goto if_7_end
if_7_else:
	iconst_0
	istore 6

if_7_end:
	iconst_5
	iload 6
	invokestatic PrintTest/printTest(IZ)V

	return
.end method


