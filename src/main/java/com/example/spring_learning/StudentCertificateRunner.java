package com.example.spring_learning;
import org.springframework.stereotype.Component;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.*;;
@Component
public class StudentCertificateRunner implements ApplicationRunner{
    private final ApplicationContext context;
    public StudentCertificateRunner(ApplicationContext context) {
        this.context = context;
    }
    @Override
    public void run(ApplicationArguments args) throws Exception {
        StudentCertificate certificate1 = context.getBean(StudentCertificate.class);
        certificate1.generateCertificate();
        StudentCertificate certificate2 = context.getBean(StudentCertificate.class);
        certificate2.generateCertificate();
        System.out.println("Are firstand second objects same?"+(certificate1==certificate2));
    }
    
}
