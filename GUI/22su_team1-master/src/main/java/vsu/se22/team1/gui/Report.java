package vsu.se22.team1.gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.util.List;
import java.util.stream.Collectors;

import vsu.se22.team1.AccessAttempt;
import vsu.se22.team1.Area;
import vsu.se22.team1.AttemptFlag;
import vsu.se22.team1.Employee;
import vsu.se22.team1.I18n;
import vsu.se22.team1.Utils;

/**
 * Utilities for generating reports.
 */
public final class Report {
    public static void accessPlain(File out, List<AccessAttempt> accessLog) throws IOException {
        StringBuilder sb = new StringBuilder();
        for(AccessAttempt a : accessLog) {
            sb.append(a.toString());
            sb.append("\n");
        }
        Files.write(out.toPath(), sb.toString().getBytes());
    }

    public static void accessHTML(File out, List<AccessAttempt> accessLog) throws IOException {
        InputStream in = Report.class.getResourceAsStream("/access-history-template.html");
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String template = reader.lines().collect(Collectors.joining("\n"));
        reader.close();
        in.close();

        StringBuilder sb = new StringBuilder();
        for(AccessAttempt a : accessLog) {
            if(a.flag == AttemptFlag.SECURITY_ALERT) {
                sb.append("<tr class=\"security-alert\"><td>");
            } else {
                sb.append("<tr><td>");
            }
            sb.append(Utils.DATE_FORMAT.format(a.timestamp));
            sb.append("</td><td>");
            sb.append(a.employeeId);
            sb.append("</td><td>");
            sb.append(a.area);
            sb.append("</td><td>");
            sb.append(a.flag);
            sb.append("</td></tr>\n");
        }

        Files.write(out.toPath(), template.replace("___TABLE_ROWS___", sb.toString()).getBytes());
    }

    public static void employeesPlain(File out, List<Employee> employees) throws IOException {
        StringBuilder sb = new StringBuilder();
        int count = employees.size();
        for(Employee e : employees) {
            sb.append(e);
            sb.append("\n");
            for(Area a : e.access.keySet()) {
                sb.append("\t");
                sb.append(a.toString());
                sb.append(": ");
                sb.append(e.hasAccess(a) ? I18n.get("attempt-flag.success") : I18n.get("attempt-flag.denied"));
                sb.append("\n");
            }
            count--;
            if(count > 0) sb.append("\n");
        }
        Files.write(out.toPath(), sb.toString().getBytes());
    }

    public static void employeesHTML(File out, List<Employee> employees) throws IOException {
        InputStream in = Report.class.getResourceAsStream("/employees-template.html");
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String template = reader.lines().collect(Collectors.joining("\n"));
        reader.close();
        in.close();

        StringBuilder sb = new StringBuilder();
        for(Employee e : employees) {
            sb.append("<h2>");
            sb.append(e.toString());
            sb.append("</h2>\n<table><tr><th>Area</th><th>Access State</th></tr>\n");
            for(Area a : e.access.keySet()) {
                sb.append("<tr><td>");
                sb.append(a.toString());
                sb.append("</td><td>");
                sb.append(e.hasAccess(a) ? I18n.get("attempt-flag.success") : I18n.get("attempt-flag.denied"));
                sb.append("</td></tr>\n");
            }
            sb.append("</table><br><hr><br>\n");
        }

        Files.write(out.toPath(), template.replace("___TABLES___", sb.toString()).getBytes());
    }
}
