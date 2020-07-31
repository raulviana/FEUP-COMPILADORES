.class public Example
.super java/lang/Object

.method public <init>()V
	aload_0
	invokenonvirtual java/lang/Object/<init>()V
	return
.end method

.method public static main([Ljava/lang/String;)V
	.limit stack 4
	.limit locals 6

	iconst_1
	istore_1

	bipush 8
	newarray int
	astore_3

	iconst_1
	istore 4

	new Example
	dup
	invokespecial Example/<init>()V
	astore 5

	aload_3
	iconst_3
	iconst_0
	iastore

	aload 5
	iload_1
	aload_3
	iload 4
	invokevirtual Example/func(I[IZ)I

	istore_2

while_1_begin:
	iload 4
	ifeq while_1_end
	iconst_0
	istore 4

	iload_2
	invokestatic ioPlus/printResult(I)V

	goto while_1_begin
while_1_end:
	return
.end method


.method public func(I[IZ)I
	.limit stack 2
	.limit locals 5

	iload_3
	ifeq if_2_else
	aload_2
	arraylength
	istore 4

	goto if_2_end
if_2_else:
	iload_1
	istore 4

if_2_end:
	iload 4
	aload_0
	invokevirtual Example/func1()I

	iadd
	istore 4

	iload 4
	ireturn
.end method


.method public func1()I
	.limit stack 2
	.limit locals 1

	iconst_2
	ireturn
.end method


