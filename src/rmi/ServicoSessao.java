package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;
import model.Sessao;

public interface ServicoSessao extends Remote {
    String NOME = "ServicoSessao";
    int PORTA = 1099;

    // Retorna a sessão do celular informado ou null se não existir
    Sessao buscarSessao(String numeroCelular) throws RemoteException;
}
