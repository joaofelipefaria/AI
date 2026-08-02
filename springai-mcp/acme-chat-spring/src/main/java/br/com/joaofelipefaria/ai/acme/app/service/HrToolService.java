package br.com.joaofelipefaria.ai.acme.app.service;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HrToolService {

    private static final Pattern EMPLOYEE_ID_PATTERN = Pattern.compile("(?:employee|id)\\s*(?:#|no\\.?|:)?\\s*(\\d+)", Pattern.CASE_INSENSITIVE);

    private final RestTemplate restTemplate;
    private final String backendBaseUrl;

    public Optional<Integer> extractEmployeeId(String question) {
        Matcher matcher = EMPLOYEE_ID_PATTERN.matcher(question);
        return matcher.find() ? Optional.of(Integer.parseInt(matcher.group(1))) : Optional.empty();
    }

    public String getEmployeeSummary(Integer employeeId) {
        String url = backendBaseUrl + "/api/employees/" + employeeId;
        return restTemplate.getForObject(url, String.class);
    }

    public String getLeavesForEmployee(Integer employeeId) {
        String url = backendBaseUrl + "/api/employees/" + employeeId + "/ferias";
        return restTemplate.getForObject(url, String.class);
    }

    public String getLeaveDaysForYear(Integer employeeId, int year) {
        String url = backendBaseUrl + "/api/employees/" + employeeId + "/ferias/dias-no-ano?ano=" + year;
        return restTemplate.getForObject(url, String.class);
    }
}
