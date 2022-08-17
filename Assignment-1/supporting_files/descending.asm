	.data
a:
	70
	80
	40
	20
	10
	30
	50
	60
n:
	8
	.text
main:
	load %x0, $n, %x3	;load n[0] to x3
	addi %x0, %x0, %x4	;i = x4
	subi %x3, 1, %x3	;x3 = n - 1
elderloop:
	beq %x4, %x3, endl	;check if i = n - 1, condition for termination
	subi %x3, $x4, %x9	; n-i-1, stored in x9
	addi %x4, 1, %x4	;incrementing i
	addi %x0, %x0, %x5	;initialize j = 0
youngloop:
	beq %x5, %x9, elderloop	;checking condition, is j equal to n-i-1
	addi %x5, 1, %x6	;x6 has j+1 
	load %x5, $a, %x7	;load a[j] into x7
	load %x6, $a, %x8	;load a[j+1] into x8
	addi %x5,%x0,%x28	;saving j value somewhere before incrementing, this is for swap
	addi %x5, 1, %x5	;incrementing j
	bgt %x8, %x7, swap	;if a[j+1]>a[j], swap time. Condition changed because we need descending order
	jmp youngloop
swap:
	store %x8,$a,%x28	;a[x28] = x8 i.e, a[j] = a[j+1]
	store %x7,$a,%x6	;a[j+1] = a[j]
	jmp youngloop
