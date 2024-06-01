import java.util.*;

public class BLUE_CHECK {

    private static HashMap<String, String> UserDatabase = new HashMap<>();
    private static HashMap<String, HashMap<String, Boolean>> schedule = new HashMap<>();
    private static HashMap<String, ArrayList<String>> subjectsPerDay = new HashMap<>();
    private static HashMap<String, String> objection = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        initializeSchedule();

        while (true) {
            System.out.println("----- LOGIN MENU -----");
            System.out.println("1. 회원 가입");
            System.out.println("2. 로그인");
            System.out.print("메뉴 선택: ");
            String choice = scanner.next();
            System.out.println();

            switch (choice) {
                case "1":
                    registerUser(scanner);
                    break;
                case "2":
                    if (login(scanner)) {
                        secondMenu(scanner);
                    } else {
                        System.out.println("로그인에 실패하였습니다.");
                        System.out.println();
                    }
                    break;
                default:
                    System.out.println("올바른 선택이 아닙니다.");
                    System.out.println();
            }
        }
    }

    private static void registerUser(Scanner scanner) {
        System.out.print("학번(10자리 숫자): ");
        String id = scanner.next();
        System.out.print("비밀번호(4자리 숫자): ");
        String password = scanner.next();
        System.out.println();
        UserDatabase.put(id, password);
        System.out.println("회원가입이 완료되었습니다.");
        System.out.println();
    }

    private static boolean login(Scanner scanner) {
        System.out.print("학번: ");
        String id = scanner.next();
        System.out.print("비밀번호: ");
        String password = scanner.next();
        System.out.println();

        return UserDatabase.containsKey(id) && UserDatabase.get(id).equals(password);
    }

    private static void initializeSchedule() {
        String[] days = {"월", "화", "수", "목", "금"};

        String[] monWedSubjects = {"과목1", "과목2"};
        String[] tueThuSubjects = {"과목3", "과목4"};
        String[] friSubjects = {"과목5", "과목6"};

        for (String day : days) {
            schedule.put(day, new HashMap<>());
            if (day.equals("월") || day.equals("수")) {
                subjectsPerDay.put(day, new ArrayList<>(Arrays.asList(monWedSubjects)));
                for (String subject : monWedSubjects) {
                    schedule.get(day).put(subject, false);
                }
            } else if (day.equals("화") || day.equals("목")) {
                subjectsPerDay.put(day, new ArrayList<>(Arrays.asList(tueThuSubjects)));
                for (String subject : tueThuSubjects) {
                    schedule.get(day).put(subject, false);
                }
            } else if (day.equals("금")) {
                subjectsPerDay.put(day, new ArrayList<>(Arrays.asList(friSubjects)));
                for (String subject : friSubjects) {
                    schedule.get(day).put(subject, false);
                }
            }
        }
    }

    private static void secondMenu(Scanner scanner) {
        while (true) {
            System.out.println("----- Attendance MENU -----");
            System.out.println("1. 출석하기");
            System.out.println("2. 출결 관리");
            System.out.println("3. 이의 신청");
            System.out.println("4. 나가기");
            System.out.print("메뉴 선택: ");
            String choice = scanner.next();
            System.out.println();

            switch (choice) {
                case "1":
                    markAttendance(scanner);
                    break;
                case "2":
                    manageAttendance(scanner);
                    break;
                case "3":
                    fileObjection(scanner);
                    break;
                case "4":
                    return;
                default:
                    System.out.println("올바른 선택이 아닙니다.");
            }
        }
    }

    private static void markAttendance(Scanner scanner) {
        System.out.println("요일을 입력하세요 (월, 화, 수, 목, 금):");
        String day = scanner.next();
        if (schedule.containsKey(day)) {
            System.out.println("과목을 선택하세요:");
            ArrayList<String> subjects = subjectsPerDay.get(day);
            for (String subject : subjects) {
                System.out.println(subject);
            }
            String subject = scanner.next();
            if (subjects.contains(subject)) {
                System.out.println("출석(1) / 실패(0):");
                String attendance = scanner.next();
                System.out.println();
                if (attendance.equals("1")) {
                    schedule.get(day).put(subject, true);
                    System.out.println("출석하였습니다.");
                } else if (attendance.equals("0")) {
                    System.out.println("위치 인증에 실패 하였습니다.");
                } else {
                    System.out.println("올바른 입력이 아닙니다.");
                }
            } else {
                System.out.println("올바른 과목이 아닙니다.");
            }
        } else {
            System.out.println("올바른 요일이 아닙니다.");
        }
        System.out.println();
    }

    private static void manageAttendance(Scanner scanner) {
        System.out.println("출결 현황:");
        HashSet<String> uniqueSubjects = new HashSet<>();
        for (Map.Entry<String, HashMap<String, Boolean>> entry : schedule.entrySet()) {
            String day = entry.getKey();
            HashMap<String, Boolean> dailySchedule = entry.getValue();
            System.out.println(day + "요일");
            for (Map.Entry<String, Boolean> subEntry : dailySchedule.entrySet()) {
                String subject = subEntry.getKey();
                uniqueSubjects.add(subject);
                System.out.println(subject + ": " + (subEntry.getValue() ? "출석" : "결석"));
            }
            System.out.println();
        }
    }

    private static void fileObjection(Scanner scanner) {
        System.out.println("이의 신청할 과목을 선택하세요:");
        HashSet<String> uniqueSubjects = new HashSet<>();
        for (Map.Entry<String, HashMap<String, Boolean>> entry : schedule.entrySet()) {
            HashMap<String, Boolean> dailySchedule = entry.getValue();
            for (String subject : dailySchedule.keySet()) {
                uniqueSubjects.add(subject);
            }
        }
        for (String subject : uniqueSubjects) {
            System.out.println(subject);
        }
        scanner.nextLine();  // consume the newline character left from previous input
        String subject = scanner.nextLine();
        System.out.println();
        if (uniqueSubjects.contains(subject)) {
            System.out.println("사유를 입력하세요 ");
            System.out.println("예시) 개인 기기 문제, 시스템 문제 등...");
            System.out.print(": ");
            String reason = scanner.nextLine();
            objection.put(subject, reason);
            System.out.println();
            System.out.println("이의 신청이 접수되었습니다.");
        } else {
            System.out.println("올바른 과목이 아닙니다.");
        }
        System.out.println();
    }
}