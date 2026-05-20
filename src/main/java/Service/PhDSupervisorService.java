public class PhDSupervisorService {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    public PhDSupervisorService(StudentRepository studentRepository,
                                TeacherRepository teacherRepository) {
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
    }

    public void assignSupervisor(String phdStudentId, Long teacherId) {
        Student student = studentRepository.findByStudentId(phdStudentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + phdStudentId));

        if (!(student instanceof PHDStudent)) {
            throw new IllegalArgumentException("Student " + phdStudentId + " is not a PhD student.");
        }

        Teacher supervisor = teacherRepository.findAll().stream()
                .filter(t -> t.getId() != null && t.getId().equals(teacherId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Teacher not found with id: " + teacherId));

        if (supervisor.getHIndex() < 3) {
            throw new SupervisorHIndexException(
                    "Supervisor h-index must be at least 3. Current h-index: " + supervisor.getHIndex());
        }

        ((PHDStudent) student).setSupervisor(supervisor);
        studentRepository.save(student);
    }
}
