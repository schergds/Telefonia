package rmi;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import model.Sessao;
import model.TabelaHash;

public class ServicoSessaoImpl extends UnicastRemoteObject implements ServicoSessao {
    private static final long serialVersionUID = 1L;

    private final TabelaHash hashSessoes;

    public ServicoSessaoImpl(TabelaHash hashSessoes) throws RemoteException {
        super();
        this.hashSessoes = hashSessoes;
    }

    @Override
    public Sessao buscarSessao(String numeroCelular) throws RemoteException {
        if (numeroCelular == null) {
            return null;
        }
        return hashSessoes.sessoes.get(numeroCelular.trim());
    }
}
