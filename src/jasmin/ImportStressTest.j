.class public ImportStressTest
.super java/lang/Object
.field private i I

.method public <init>()V
	aload_0
	invokenonvirtual java/lang/Object/<init>()V
	return
.end method

.method public static main([Ljava/lang/String;)V
	.limit stack 4
	.limit locals 3

	iconst_0
	istore_2

	new List
	dup
	invokespecial List/<init>()V
	astore_1

	new List
	dup
	iconst_1
	invokespecial List/<init>(I)V
	invokevirtual List/size()I

	istore_2

	aload_1
	invokevirtual List/size()I

	istore_2

	iconst_1
	istore_2

	new Map
	dup
	iload_2
	iload_2
	invokespecial Map/<init>(II)V
	pop
	new ImportStressTest
	dup
	invokespecial ImportStressTest/<init>()V
	pop
	return
.end method


