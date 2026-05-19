import java.util.List;
import java.util.Scanner;

/**
 * Интерфейс для student organizations.
 * Есть два режима: для студента и для админа, потому что действия почти одинаковые,
 * но права разные.
 */
public class OrganizationUI {

    private final ApplicationContext ctx;
    private final Scanner sc;

    public OrganizationUI(ApplicationContext ctx, Scanner sc) {
        this.ctx = ctx;
        this.sc = sc;
    }

    public void runForStudent(Student student) {
        // Студент может быть просто участником, а может быть leader и смотреть заявки.
        boolean running = true;
        while (running) {
            ConsoleUtils.printHeader("Organizations");
            System.out.println("  1. Browse organizations");
            System.out.println("  2. My organizations");
            System.out.println("  3. Create organization");
            System.out.println("  4. Apply for membership");
            System.out.println("  5. Leader: pending applications");
            System.out.println("  6. Leader: approve application");
            System.out.println("  7. Leader: reject application");
            System.out.println("  0. Back");

            int choice = ConsoleUtils.readIntInRange(sc, "\n  Choose: ", 0, 7);
            switch (choice) {
                case 1: browseOrganizations(); break;
                case 2: showStudentOrganizations(student); break;
                case 3: createOrganization(student); break;
                case 4: apply(student); break;
                case 5: showPendingApplications(); break;
                case 6: approveApplication(student); break;
                case 7: rejectApplication(student); break;
                case 0: running = false; break;
            }
        }
    }

    public void runForAdmin() {
        // Админский режим больше для тех поддержки данных, без проверки leader.
        boolean running = true;
        while (running) {
            ConsoleUtils.printHeader("Organization Management");
            System.out.println("  1. Browse active organizations");
            System.out.println("  2. Search organizations");
            System.out.println("  3. Update organization");
            System.out.println("  4. Deactivate organization");
            System.out.println("  5. Members");
            System.out.println("  6. Add member");
            System.out.println("  7. Remove member");
            System.out.println("  8. Change member role");
            System.out.println("  0. Back");

            int choice = ConsoleUtils.readIntInRange(sc, "\n  Choose: ", 0, 8);
            switch (choice) {
                case 1: browseOrganizations(); break;
                case 2: searchOrganizations(); break;
                case 3: updateOrganization(); break;
                case 4: deleteOrganization(); break;
                case 5: showMembers(); break;
                case 6: addMember(); break;
                case 7: removeMember(); break;
                case 8: changeRole(); break;
                case 0: running = false; break;
            }
        }
    }

    private void browseOrganizations() {
        ConsoleUtils.printHeader("Active Organizations");
        printOrganizations(ctx.getOrganizationService().getAllActiveOrganizations());
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void searchOrganizations() {
        ConsoleUtils.printHeader("Search Organizations");
        String keyword = ConsoleUtils.readLine(sc, "  Name contains: ");
        printOrganizations(ctx.getOrganizationService().searchOrganizations(keyword));
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void showStudentOrganizations(Student student) {
        ConsoleUtils.printHeader("My Organizations");
        printOrganizations(ctx.getOrganizationService().getStudentOrganizations(student.getId()));
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void createOrganization(Student student) {
        ConsoleUtils.printHeader("Create Organization");
        try {
            String name = ConsoleUtils.readNonBlank(sc, "  Name: ");
            String description = ConsoleUtils.readNonBlank(sc, "  Description: ");
            Organization org = ctx.getOrganizationService().createOrganization(
                    new OrganizationCreationRequest(name, description),
                    student.getStudentId());
            ConsoleUtils.printSuccess("Organization #" + org.getId() + " created.");
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void updateOrganization() {
        ConsoleUtils.printHeader("Update Organization");
        try {
            Long orgId = Long.parseLong(ConsoleUtils.readNonBlank(sc, "  Organization ID: "));
            String name = ConsoleUtils.readLine(sc, "  New name (optional): ");
            String description = ConsoleUtils.readLine(sc, "  New description (optional): ");
            ctx.getOrganizationService().updateOrganization(
                    orgId,
                    new OrganizationUpdateRequest(
                            name.isBlank() ? null : name,
                            description.isBlank() ? null : description));
            ConsoleUtils.printSuccess("Organization updated.");
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void deleteOrganization() {
        ConsoleUtils.printHeader("Deactivate Organization");
        try {
            Long orgId = Long.parseLong(ConsoleUtils.readNonBlank(sc, "  Organization ID: "));
            ctx.getOrganizationService().deleteOrganization(orgId);
            ConsoleUtils.printSuccess("Organization deactivated.");
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void apply(Student student) {
        ConsoleUtils.printHeader("Apply For Membership");
        try {
            Long orgId = Long.parseLong(ConsoleUtils.readNonBlank(sc, "  Organization ID: "));
            String body = ConsoleUtils.readNonBlank(sc, "  Application text: ");
            MembershipApplication app = ctx.getOrganizationService().applyForMembership(
                    student.getStudentId(), orgId, body);
            ConsoleUtils.printSuccess("Application #" + app.getId() + " submitted.");
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void showPendingApplications() {
        ConsoleUtils.printHeader("Pending Applications");
        try {
            Long orgId = Long.parseLong(ConsoleUtils.readNonBlank(sc, "  Organization ID: "));
            List<MembershipApplication> apps = ctx.getOrganizationService().getPendingApplications(orgId);
            if (apps.isEmpty()) {
                System.out.println("  No pending applications.");
            } else {
                apps.forEach(a -> System.out.printf("  #%-3d student=%s status=%s body=%s%n",
                        a.getId(),
                        a.getStudent() != null ? a.getStudent().getStudentId() : "?",
                        a.getApplicationStatus(),
                        a.getBody()));
            }
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void approveApplication(Student leader) {
        ConsoleUtils.printHeader("Approve Application");
        try {
            Long applicationId = Long.parseLong(ConsoleUtils.readNonBlank(sc, "  Application ID: "));
            ctx.getOrganizationService().approveApplication(applicationId, leader.getId());
            ConsoleUtils.printSuccess("Application approved.");
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void rejectApplication(Student leader) {
        ConsoleUtils.printHeader("Reject Application");
        try {
            Long applicationId = Long.parseLong(ConsoleUtils.readNonBlank(sc, "  Application ID: "));
            ctx.getOrganizationService().rejectApplication(applicationId, leader.getId());
            ConsoleUtils.printSuccess("Application rejected.");
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void showMembers() {
        ConsoleUtils.printHeader("Organization Members");
        try {
            Long orgId = Long.parseLong(ConsoleUtils.readNonBlank(sc, "  Organization ID: "));
            List<Membership> members = ctx.getOrganizationService().getOrganizationMembers(orgId);
            if (members.isEmpty()) {
                System.out.println("  No active members.");
            } else {
                members.forEach(this::printMembership);
            }
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void addMember() {
        ConsoleUtils.printHeader("Add Member");
        try {
            String studentId = ConsoleUtils.readNonBlank(sc, "  Student ID: ");
            Long orgId = Long.parseLong(ConsoleUtils.readNonBlank(sc, "  Organization ID: "));
            MembershipRole role = readMembershipRole();
            ctx.getOrganizationService().addMember(studentId, orgId, role);
            ConsoleUtils.printSuccess("Member added.");
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void removeMember() {
        ConsoleUtils.printHeader("Remove Member");
        try {
            Long membershipId = Long.parseLong(ConsoleUtils.readNonBlank(sc, "  Membership ID: "));
            ctx.getOrganizationService().removeMember(membershipId);
            ConsoleUtils.printSuccess("Member deactivated.");
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private void changeRole() {
        ConsoleUtils.printHeader("Change Member Role");
        try {
            Long membershipId = Long.parseLong(ConsoleUtils.readNonBlank(sc, "  Membership ID: "));
            MembershipRole role = readMembershipRole();
            ctx.getOrganizationService().changeMemberRole(membershipId, role);
            ConsoleUtils.printSuccess("Role changed.");
        } catch (Exception ex) {
            ConsoleUtils.printError(ex.getMessage());
        }
        ConsoleUtils.pressEnterToContinue(sc);
    }

    private MembershipRole readMembershipRole() {
        MembershipRole[] values = MembershipRole.values();
        for (int i = 0; i < values.length; i++) {
            System.out.printf("  %d. %s%n", i + 1, values[i]);
        }
        int choice = ConsoleUtils.readIntInRange(sc, "  Role: ", 1, values.length);
        return values[choice - 1];
    }

    private void printOrganizations(List<Organization> organizations) {
        if (organizations.isEmpty()) {
            System.out.println("  No organizations found.");
            return;
        }
        organizations.forEach(o -> System.out.printf("  #%-3d %-25s leader=%s active=%s%n",
                o.getId(),
                o.getName(),
                o.getLeader() != null ? o.getLeader().getFullName() : "-",
                o.getIsActive()));
    }

    private void printMembership(Membership membership) {
        System.out.printf("  #%-3d student=%s role=%s status=%s%n",
                membership.getId(),
                membership.getStudent() != null ? membership.getStudent().getStudentId() : "?",
                membership.getRole(),
                membership.getStatus());
    }
}
