package ua.com.foxminded.school.providers;

import com.google.inject.Inject;

import java.util.Scanner;

public class ViewProviderImpl implements ViewProvider{

    @Inject
    public ViewProviderImpl() {
    }

    @Override
    public void printMessage(String message) {
        System.out.println(message);
    }

    @Override
    public String read() {
        Scanner sc = new Scanner(System.in);
        return sc.nextLine();
    }

    @Override
    public int readInt() {
        Scanner sc = new Scanner(System.in);
        return sc.nextInt();
    }
}
