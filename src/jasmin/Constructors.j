.class public Constructors
.super java/lang/Object

.method public <init>()V
	aload_0
	invokenonvirtual java/lang/Object/<init>()V
	return
.end method

.method public static main([Ljava/lang/String;)V
	.limit stack 4
	.limit locals 4

	bipush 15
	istore_1

	bipush 20
	istore_2

	new Person
	dup
	iload_1
	iload_2
	invokespecial Person/<init>(II)V
	astore_3

	aload_3
	invokevirtual Person/printInfo()V

while_1_begin:
	aload_3
	invokevirtual Person/getAge()I

	bipush 25
	if_icmpge while_1_end
	aload_3
	invokevirtual Person/getId()I

	iconst_2
	if_icmpge if_2_else
	aload_3
	aload_3
	invokevirtual Person/getAge()I

	iconst_1
	iadd
	invokevirtual Person/setAge(I)V

	aload_3
	invokevirtual Person/printInfo()V

	goto if_2_end
if_2_else:
	aload_3
	aload_3
	invokevirtual Person/getId()I

	iconst_3
	isub
	invokevirtual Person/setId(I)V

	aload_3
	invokevirtual Person/printInfo()V

if_2_end:
	goto while_1_begin
while_1_end:
	return
.end method


