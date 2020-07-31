.class public Array2
.super java/lang/Object
.field private a [I
.field private c I

.method public <init>()V
	aload_0
	invokenonvirtual java/lang/Object/<init>()V
	return
.end method

.method public test()I
	.limit stack 2
	.limit locals 1

	aload_0
	getfield Array2/c I
	ireturn
.end method


.method public test(I)I
	.limit stack 3
	.limit locals 2

	aload_0
	iconst_1
	putfield Array2/c I

	aload_0
	bipush 8
	newarray int
	putfield Array2/a [I

	aload_0
	getfield Array2/a [I
	iconst_0
	iconst_2
	iastore

	aload_0
	getfield Array2/a [I
	pop
	aload_0
	getfield Array2/a [I
	iconst_0
	iaload
	invokestatic io/print(I)V

	iconst_1
	ireturn
.end method


.method public static main([Ljava/lang/String;)V
	.limit stack 4
	.limit locals 3

	bipush 8
	newarray int
	astore_1

	aload_1
	iconst_0
	iconst_2
	iastore

	aload_1
	invokestatic io/printArray([I)V

	aload_1
	iconst_0
	iaload
	invokestatic io/print(I)V

	new Map
	dup
	iconst_1
	iconst_1
	invokespecial Map/<init>(II)V
	astore_2

	return
.end method


