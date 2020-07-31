.class public ArrayTest
.super java/lang/Object
.field private array [I
.field private array_len I

.method public <init>()V
	aload_0
	invokenonvirtual java/lang/Object/<init>()V
	return
.end method

.method public get_array()[I
	.limit stack 2
	.limit locals 1

	aload_0
	getfield ArrayTest/array [I
	areturn
.end method


.method public initialize_array()I
	.limit stack 3
	.limit locals 1

	aload_0
	bipush 10
	newarray int
	putfield ArrayTest/array [I

	aload_0
	aload_0
	getfield ArrayTest/array [I
	arraylength
	putfield ArrayTest/array_len I

	iconst_0
	ireturn
.end method


.method public fill_array()Z
	.limit stack 3
	.limit locals 1

	aload_0
	getfield ArrayTest/array [I
	iconst_0
	iconst_1
	iastore

	aload_0
	getfield ArrayTest/array [I
	iconst_1
	iconst_2
	iastore

	aload_0
	getfield ArrayTest/array [I
	iconst_2
	iconst_3
	iastore

	aload_0
	getfield ArrayTest/array [I
	iconst_3
	iconst_4
	iastore

	aload_0
	getfield ArrayTest/array [I
	iconst_4
	iconst_5
	iastore

	aload_0
	getfield ArrayTest/array [I
	iconst_5
	bipush 6
	iastore

	aload_0
	getfield ArrayTest/array [I
	bipush 6
	bipush 7
	iastore

	aload_0
	getfield ArrayTest/array [I
	bipush 7
	bipush 8
	iastore

	aload_0
	getfield ArrayTest/array [I
	bipush 8
	bipush 9
	iastore

	aload_0
	getfield ArrayTest/array [I
	bipush 9
	bipush 10
	iastore

	iconst_1
	ireturn
.end method


.method public print_length()I
	.limit stack 2
	.limit locals 3

	new PrintArray
	dup
	invokespecial PrintArray/<init>()V
	astore_1

	aload_0
	getfield ArrayTest/array_len I
	istore_2

	aload_1
	iload_2
	invokevirtual PrintArray/printLength(I)V

	iload_2
	ireturn
.end method


.method public setValue(II)I
	.limit stack 3
	.limit locals 3

	aload_0
	getfield ArrayTest/array [I
	iload_1
	iload_2
	iastore

	iconst_0
	ireturn
.end method


.method public test_array_access()I
	.limit stack 3
	.limit locals 3

	new PrintArray
	dup
	invokespecial PrintArray/<init>()V
	astore_1

	aload_0
	getfield ArrayTest/array [I
	iconst_2
	iaload
	istore_2

	aload_1
	iconst_3
	iload_2
	invokevirtual PrintArray/printElement(II)V

	aload_0
	getfield ArrayTest/array [I
	iconst_2
	bipush 100
	iastore

	aload_0
	getfield ArrayTest/array [I
	iconst_2
	iaload
	istore_2

	aload_1
	iconst_3
	iload_2
	invokevirtual PrintArray/printElement(II)V

	iconst_0
	ireturn
.end method


.method public print_array()Z
	.limit stack 4
	.limit locals 3

	new PrintArray
	dup
	invokespecial PrintArray/<init>()V
	astore_1

	iconst_0
	istore_2

	aload_1
	invokevirtual PrintArray/printTitle()V

while_1_begin:
	iload_2
	aload_0
	getfield ArrayTest/array_len I
	if_icmpge while_1_end
	aload_1
	iload_2
	iconst_1
	iadd
	aload_0
	getfield ArrayTest/array [I
	iload_2
	iaload
	invokevirtual PrintArray/printElement(II)V

	iinc 2 1

	goto while_1_begin
while_1_end:
	iconst_1
	ireturn
.end method


.method public static main([Ljava/lang/String;)V
	.limit stack 3
	.limit locals 5

	new ArrayTest
	dup
	invokespecial ArrayTest/<init>()V
	astore_1

	new PrintArray
	dup
	invokespecial PrintArray/<init>()V
	astore_2

	iconst_5
	newarray int
	astore_3

	iconst_0
	istore 4

	aload_1
	invokevirtual ArrayTest/initialize_array()I

	pop
	aload_1
	invokevirtual ArrayTest/fill_array()Z

	pop
	aload_1
	invokevirtual ArrayTest/print_length()I

	pop
	aload_1
	invokevirtual ArrayTest/test_array_access()I

	pop
	aload_1
	invokevirtual ArrayTest/print_array()Z

	pop
	aload_1
	iconst_0
	bipush 20
	invokevirtual ArrayTest/setValue(II)I

	pop
	aload_1
	invokevirtual ArrayTest/print_array()Z

	pop
while_2_begin:
	iload 4
	aload_3
	arraylength
	if_icmpge while_2_end
	aload_3
	iload 4
	iconst_0
	iastore

	iinc 4 1

	goto while_2_begin
while_2_end:
	aload_2
	aload_3
	invokevirtual PrintArray/printArray([I)V

	aload_3
	iconst_4
	iconst_4
	iastore

	aload_2
	aload_3
	invokevirtual PrintArray/printArray([I)V

	return
.end method


