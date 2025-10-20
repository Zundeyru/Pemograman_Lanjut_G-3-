class Hospital {
    public String hospitalName;
    public String address;
    public Patient patient;

    public Hospital(String hospitalName, String address, Patient patient) {
        this.hospitalName = hospitalName;
        this.address = address;
        this.patient = patient;
    }

    public void printHospitalDetails() {
        System.out.println("Hospital Name : " + hospitalName);
        System.out.println("Address       : " + address);
        patient.printPatientDetails();
    }
}

class Doctor {
    public String name;
    public int id;
    public double salary;
    public String specialization;

    public Doctor(String name, int id, double salary, String specialization) {
        this.name = name;
        this.id = id;
        this.salary = salary;
        this.specialization = specialization;
    }

    public void applyBonus() {
        // contoh sederhana: bonus 10%
        salary += salary * 0.10;
        System.out.println("Bonus applied. New salary: " + salary);
    }

    public void printDetails() {
        System.out.println("Doctor Name   : " + name);
        System.out.println("Doctor ID     : " + id);
        System.out.println("Specialization: " + specialization);
        System.out.println("Salary        : " + salary);
    }
}

class Patient {
    public String name;
    public int patientId;
    public Doctor doctor;
    public String diagnosis;

    public Patient(String name, int patientId, Doctor doctor, String diagnosis) {
        this.name = name;
        this.patientId = patientId;
        this.doctor = doctor;
        this.diagnosis = diagnosis;
    }

    public void printPatientDetails() {
        System.out.println("Patient Name  : " + name);
        System.out.println("Patient ID    : " + patientId);
        System.out.println("Diagnosis     : " + diagnosis);
        System.out.println("--- Attending Doctor ---");
        doctor.printDetails();
    }
}

public class MainApp {
    public static void main(String[] args) {
        Doctor doctor = new Doctor("Dr. Sarah Lee", 2001, 12000, "Cardiology");
        Patient patient = new Patient("Michael Brown", 555, doctor, "Heart Disease");

        Hospital hospital = new Hospital("City General Hospital", "123 Main Street", patient);
        hospital.printHospitalDetails();

        System.out.println();+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
                6.3
        doctor.applyBonus();
        doctor.printDetails();
    }
}
