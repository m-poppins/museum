import java.sql.DriverManager;
import java.util.*;
public class ConsoleApp {
	private PeopleTable pt;
	private PhonesTable pht;
	private Scanner in;
	private int person_id;
	private String[] person_obj;

	public void create()
	{
		pt.create();
		pht.create();
	}
	public void drop()
	{
		pht.drop();
		pt.drop();
	}
	public ConsoleApp()
	{
		String password;
		String user;
		in = new Scanner(System.in);
		System.out.println("Введите пользователя БД:");
		user = in.next();
		System.out.println("Введите пароль пользователя БД:");
		password = in.next();
		DBConnection con = DBConnection.connect(user, password);
		while(con.cbase == null) {
			System.out.println("Указана неверная комбинация пользовательское имя/пароль! Повторите ввод!");
			System.out.println("Введите пользователя БД:");
			user = in.next();
			System.out.println("Введите пароль пользователя БД:");
			password = in.next();
			con = DBConnection.reconnect(user, password);
		}
		pt = new PeopleTable();
		pht = new PhonesTable();
	}
	public void initialize()
	{
		try {
			drop();
			create();
		} catch(Exception e){
			System.out.println(e);
		}
	}
	public void show_main_menu()
	{
		System.out.print(
			"Добро пожаловать!\n" +
			"Основное меню (выберите цифру в соответствии с необходимым действием):\n" +
			"1 - просмотр коллекций;\n" +
			"2 - сброс и инициализация таблиц;\n" +
			"9 - выход.\n"
		);
	}
	public int after_main_menu(int next_step)
	{
		if(next_step == 2) {
			initialize();
			System.out.println("Таблицы созданы заново!");
			return 0;
		}
		else if(next_step != 1 && next_step != 9)
		{
			System.out.println("Выбрано неверное число! Повторите ввод!");
			return 0;
		}
	return next_step;
	}

	public void show_people()
	{
 		this.person_id = -1;
		System.out.print(
		"Просмотр списка коллекций!\n" +
		"№\tНазвание\tДата начала\tДата окончания\tОписание\n"
	);
	String[][] list = pt.all();
	for(int i = 0; i < list.length; i++) {
		System.out.print(
			list[i][0] + "\t" +
			list[i][1] + "\t" +
			list[i][2] + "\t" +
			list[i][3] + "\t" +
			list[i][4] + "\n"
		);
	}
	System.out.print(
		"Дальнейшие операции:\n" +
		"0 - возврат в главное меню;\n" +
		"3 - добавление новой коллекции;\n" +
		"4 - удаление коллекции;\n" +
		"5 - просмотр экспонатов коллекции;\n" +
		"9 - выход.\n"
	);
	}


	public int show_phones_by_people(Scanner in)
	{
	 	String[] data = new String[3];
	 	String[] person;
	 	String[][] list;
	 	if(this.person_id == -1){
	 		in.nextLine();
	 		while(true) {
	 			System.out.println("Укажите номер строки, в которой записана интересующая Вас коллекция (0 - отмена):");
	 			data[0] = in.nextLine();
				while(data[0].trim().length() == 0) {
					System.out.println("Пустая строка. Повторите ввод! Укажите номер строки, в которой записана интересующая Вас коллекция (0 - отмена):");
	 				data[0] = in.nextLine();
	 			}
	 			if(data[0].equals("0")) return 1;

				person = pt.find_by_position(Integer.parseInt(data[0].trim()));
	 			// System.out.println(person[1]);
	 			if(person[0].length() == 0) {
	 				System.out.println("Введено число, неудовлетворяющее количеству коллекций!");
	 				System.out.println(data[0]);
	 			}
	 			else {
	 				this.person_id = Integer.parseInt(person[0]);
					this.person_obj = person;
	 				break;
	 			}
	 		}
	 	}
		System.out.println("Выбрана коллекция: " + this.person_obj[1] + " " +
		this.person_obj[2] + " " + this.person_obj[3]);
	 	System.out.println("Экспонаты:");
		System.out.println("Название\tСтоимость\tВек\tЗал\n");
	 	list = pht.all_by_person_id(this.person_id);
	 	for(int i = 0; i < list.length; i++) {
	 		System.out.print(
	 		list[i][1] + "\t"+
			list[i][2] + "\t"+
	 		list[i][3] + "\t"+
	 		list[i][4] + "\n"
	 		);
	 	}
	 	System.out.print(
	 	"Дальнейшие операции:\n" +
	 	"0 - возврат в главное меню;\n" +
	 	"1 - возврат в просмотр коллекций;\n" +
	 	"6 - добавление нового экспоната;\n" +
	 	"7 - удаление экспоната;\n" +
	 	"9 - выход.\n"
	 	);
	 	return read_next_step(in);
	}


	public int del_col(Scanner in)
	{
	 	String[] data = new String[3];
	 	// String[][] list;
	 	if(this.person_id == -1){
	 		in.nextLine();
	 		while(true) {
	 			System.out.println("Укажите номер строки, в которой записана интересующая Вас коллекция (0 - отмена):");
	 			data[0] = in.nextLine();
	 			while(data[0].trim().length() == 0) {
	 				System.out.println("Пустая строка. Повторите ввод! Укажите номер строки, в которой записана интересующая Вас коллекция (0 - отмена):");
	 				data[0] = in.nextLine();
	 			}
	 			if(data[0].equals("0")) return 1;
	 			break;
	 		}
	 		pt.del_one(Integer.parseInt(data[0].trim()));
			/* if(person[0].length() == 0) {
			System.out.println("Введено число, неудовлетворяющее количеству людей!");
			System.out.println(data[0]);
			}
			else {
			this.person_id = Integer.parseInt(person[0]);
			this.person_obj = person;
			break;
			}*/
	 	}
	 
		//System.out.println("Выбран человек: " + this.person_obj[1] + " " + this.person_obj[2] + " " + this.person_obj[3]);
		//System.out.println("Телефоны:");
		// list = pht.col_id_del(this.person_id);
		// for(int i = 0; i < list.length; i++) {
		// System.out.print(
		// list[i][1] + "\n"
		// );
		// }
		System.out.print(
	 	"Дальнейшие операции:\n" +
	 	"0 - возврат в главное меню;\n" +
	 	"1 - возврат в просмотр коллекций;\n" +
	 	"6 - добавление нового экспоната;\n" +
	 	"7 - удаление экспоната;\n" +
	 	"9 - выход.\n"
	 	);
	 	return read_next_step(in);
	}

	public int del_ex(Scanner in)// тут будет удаление экспоната
	{
		String[] data = new String[3];
		// String[][] list;
	 	int a = this.person_id;
	 	//if(this.person_id == -1){

		in.nextLine();
	 	while(true) {
	 		System.out.println("Укажите номер строки экспоната, который вы желаете удалить (0 - отмена):");
			data[0] = in.nextLine();
	 		while(data[0].trim().length() == 0) {
	 			System.out.println("Пустая строка. Повторите ввод! Укажите номер строки, в которой записан интересующий вас экспонат (0 - отмена):");
	 			data[0] = in.nextLine();
			}
	 		if(data[0].equals("0")) return 1;
	 		break;
	 	}
	 	pht.del_one_ex(Integer.parseInt(data[0].trim()),a);
		/* if(person[0].length() == 0) {
		System.out.println("Введено число, неудовлетворяющее количеству людей!");
		System.out.println(data[0]);
		}
		else {
		this.person_id = Integer.parseInt(person[0]);
		this.person_obj = person;
		break;
		}*/
		// }

		//System.out.println("Выбран человек: " + this.person_obj[1] + " " + this.person_obj[2] + " " + this.person_obj[3]);
		//System.out.println("Телефоны:");
		// list = pht.col_id_del(this.person_id);
		// for(int i = 0; i < list.length; i++) {
		// System.out.print(
		// list[i][1] + "\n"
		// );
		// }
		System.out.print(
		"Дальнейшие операции:\n" +
		"0 - возврат в главное меню;\n" +
		"1 - возврат в просмотр коллекций;\n" +
		"6 - добавление нового экспоната;\n" +
		"7 - удаление экспоната;\n" +
		"9 - выход.\n"
		);
		return read_next_step(in);
	}


	public int after_show_people(int next_step, Scanner in)
	{
		while(true){
			if(next_step == 4)
			{
	 			next_step = del_col(in);

				//System.out.println("Пока не реализовано!");
				// return 1;
	 		}
	 
			else if(next_step == 6)
			{
				show_add_ex(in);
				System.out.print(
				"Дальнейшие операции:\n" +
				"0 - возврат в главное меню;\n" +
				"1 - возврат в просмотр коллекций;\n" +
				"6 - добавление нового экспоната;\n" +
				"7 - удаление экспоната;\n" +
				"9 - выход.\n"
				);
				next_step = read_next_step(in);

	 			// next_step = read_next_step(in);

	 		}

	 		else if(next_step == 7)
	 		{
	 			next_step = del_ex(in);
			 // next_step = read_next_step(in);
	 		}
	 		else if(next_step == 5)
	 		{
	 			next_step = show_phones_by_people(in);
	 		}
	 		else if(next_step != 0 && next_step != 9 && next_step != 3)
	 		{
	 			System.out.println("Выбрано неверное число! Повторите ввод!");
	 			return 1;
	 		}
	 		else {
	 			return next_step;
	 		}
	 	}
		//return next_step;
	}

	public int read_next_step(Scanner in)
	{
		return in.nextInt();
	}

	public void show_add_person(Scanner in)
	{
		int flag1=0;
		int flag2=0;

		String[] data = new String[4];
		in.nextLine();
		System.out.println("Введите название (1 - отмена):");
		data[0] = in.nextLine();
		if(data[0].equals("1")) return;
		//while (flag1==0||flag2==0 ) {
		while(data[0].trim().length() == 0||data[0].trim().length()> 64) {
			if(data[0].trim().length() == 0)
				System.out.println("Название не может быть пустым! Введите название заново (1 - отмена):");
			if(data[0].trim().length()> 64)
				System.out.println("Название слишком длинное! Введите название заново (1 - отмена):");
			data[0] = in.nextLine();
			if(data[0].equals("1")) return;
		}
		/*while(data[0].trim().length()> 64) {
		System.out.println("Название слишком длинное! Введите название
		заново (1 - отмена):");
		data[0] = in.nextLine();
		if(data[0].equals("1")) return;
		}*/
		//}
		System.out.println("Введите дату начала (-1 - отмена):");
		data[1] = in.nextLine();
		if(data[1].equals("-1")) return;
		while(data[1].trim().length() == 0) {
			System.out.println("Дата начала не может быть пустой! Введите дату начала заново (-1 - отмена):");
			data[1] = in.nextLine();
			if(data[1].equals("-1")) return;
		}
		System.out.println("Введите дату окончания (-1 - отмена):");
		data[2] = in.nextLine();
		if(data[2].equals("-1")) return;
		while(data[2].trim().length() == 0) {
			System.out.println("Дата окончания не может быть пустой! Введите дату окончания заново (-1 - отмена):");
			data[2] = in.nextLine();
			if(data[2].equals("-1")) return;
		}
		System.out.println("Введите описание коллекции (1 - отмена):");
		data[3] = in.nextLine();
		if(data[3].equals("1")) return;
		while(data[3].trim().length() > 1000) {
			System.out.println("Описание коллекции больше 1000 символов! Введите описание коллекции заново (1 - отмена):");
			data[3] = in.nextLine();
			if(data[3].equals("1")) return;
		}
		pt.create_one(data);
	}

	public void show_add_ex(Scanner in) //добавляем экспонат
	{

		String[] data = new String[5];
		in.nextLine();
		System.out.println("Введите название (1 - отмена):");
		data[0] = in.nextLine();
		if(data[0].equals("1")) return;
		while(data[0].trim().length() == 0||data[0].trim().length() >1000) {
			if(data[0].trim().length() == 0)
			System.out.println("Название не может быть пустым! Введите название заново (1 - отмена):");
			if(data[0].trim().length() >1000)
			System.out.println("Название длиннее 1000 символов! Введите название заново (1 - отмена):");
			data[0] = in.nextLine();
			if(data[0].equals("1")) return;
		}
		System.out.println("Введите страховую стоимость (-1 - отмена):");
		data[1] = in.nextLine();
		if(data[1].equals("-1")) return;
		while(data[1].trim().length() == 0) {
			System.out.println("Страховая стоимость не может быть пустой! Введите страховую стоимость заново (-1 - отмена):");
			data[1] = in.nextLine();
			if(data[1].equals("-1")) return;
		}
		System.out.println("Введите век создания (z - отмена):");
		data[2] = in.nextLine();
		if(data[2].equals("z")) return;
		while(data[2].trim().length() == 0) {
			System.out.println("Век создания не может быть пустой! Введите век создания заново (z - отмена):");
			data[2] = in.nextLine();
			if(data[2].equals("z")) return;
		}
		// System.out.println("Введите номер коллекции (1 - отмена):");
		// data[3] = in.nextLine();
		data[3] = String.valueOf(this.person_id);
		// if(data[3].equals("1")) return;
		// while(data[3].trim().length() == 0) {
		// System.out.println("Номер коллекции не может быть пустой! Введите номер коллекции заново (1 - отмена):");
		// data[3] = in.nextLine();
		// if(data[3].equals("1"))
		// }
		System.out.println("Введите номер зала (-1 - отмена):");
		data[4] = in.nextLine();
		if(data[4].equals("-1")) return;
		while(data[4].trim().length() == 0) {
			System.out.println("Номер зала не может быть пустой! Введите номер зала заново (-1 - отмена):");
			data[4] = in.nextLine();
			if(data[4].equals("-1")) {
				System.out.print(
				"Дальнейшие операции:\n" +
				"0 - возврат в главное меню;\n" +
				"1 - возврат в просмотр коллекций;\n" +
				"6 - добавление нового экспоната;\n" +
				"7 - удаление экспоната;\n" +
				"9 - выход.\n"
				);
				return;
			}
		}
		pht.create_one_ex(data);
		System.out.print(
		"Дальнейшие операции:\n" +
		"0 - возврат в главное меню;\n" +
		"1 - возврат в просмотр коллекций;\n" +
		"6 - добавление нового экспоната;\n" +
		"7 - удаление экспоната;\n" +
		"9 - выход.\n"
		);
		// return read_next_step(in);
	}

	public void main_cycle()
	{
		int current_menu = 0;
		int next_step;
		while(current_menu != 9)
		{
			switch(current_menu){
				case 0:
					show_main_menu();
					next_step = read_next_step(in);
					current_menu = after_main_menu(next_step);
					break;
				case 1:
					show_people();
					next_step = read_next_step(in);
					current_menu = after_show_people(next_step, in);
					break;
				case 2:
					show_main_menu();
					break;
				case 3:
					show_add_person(in);
					current_menu = 1;
					break;
			}
		}
		System.out.println("До свидания!");
		in.close();
	}

	public static void main(String args[])
	{
		ConsoleApp ca = new ConsoleApp();
		ca.main_cycle();
		DBConnection.disconnect();
	}
}
