select * from student
	where
		age > 10 and age < 28;


select name from student;

select name from student
	where
		name like '%о%';

select * from student
	where
		id > age;

select age, name from student
	order by age;
