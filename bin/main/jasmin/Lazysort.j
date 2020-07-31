.class public Lazysort
.super Quicksort

.method public <init>()V
	aload_0
	invokenonvirtual Quicksort/<init>()V
	return
.end method

.method public static main([Ljava/lang/String;)V
	.limit stack 4
	.limit locals 5

	bipush 10
	newarray int
	astore_1

	iconst_0
	istore_2

while_1_begin:
	iload_2
	aload_1
	arraylength
	if_icmpge while_1_end
	aload_1
	iload_2
	aload_1
	arraylength
	iload_2
	isub
	iastore

	iinc 2 1

	goto while_1_begin
while_1_end:
	new Lazysort
	dup
	invokespecial Lazysort/<init>()V
	astore 4

	aload 4
	aload_1
	invokevirtual Lazysort/quicksort([I)Z

	pop
	aload 4
	aload_1
	invokevirtual Lazysort/printL([I)V

	istore_3

	return
.end method


.method public quicksort([I)Z
	.limit stack 5
	.limit locals 3

	iconst_0
	iconst_5
	invokestatic MathUtils/random(II)I

	iconst_4
	if_icmpge if_2_else
	aload_0
	aload_1
	invokevirtual Lazysort/beLazy([I)Z

	pop
	iconst_1
	istore_2

	goto if_2_end
if_2_else:
	iconst_0
	istore_2

if_2_end:
	iload_2
	ifeq if_3_else
	iload_2
	ifne negation_4
	iconst_1
	goto negation_4_end
negation_4:
	iconst_0
negation_4_end:
	istore_2

	goto if_3_end
if_3_else:
	aload_0
	aload_1
	iconst_0
	aload_1
	arraylength
	iconst_1
	isub
	invokevirtual Lazysort/quicksort([III)V

	istore_2

if_3_end:
	iload_2
	ireturn
.end method


.method public beLazy([I)Z
	.limit stack 4
	.limit locals 4

	aload_1
	arraylength
	istore_2

	iconst_0
	istore_3

while_5_begin:
	iload_3
	iload_2
	iconst_2
	idiv
	if_icmpge while_5_end
	aload_1
	iload_3
	iconst_0
	bipush 10
	invokestatic MathUtils/random(II)I

	iastore

	iinc 3 1

	goto while_5_begin
while_5_end:
while_6_begin:
	iload_3
	iload_2
	if_icmpge while_6_end
	aload_1
	iload_3
	iconst_0
	bipush 10
	invokestatic MathUtils/random(II)I

	iconst_1
	iadd
	iastore

	iinc 3 1

	goto while_6_begin
while_6_end:
	iconst_1
	ireturn
.end method


.method public a(II)I
	.limit stack 2
	.limit locals 3

	iconst_1
	ireturn
.end method


.method public a(I)I
	.limit stack 2
	.limit locals 2

	iconst_1
	ireturn
.end method


