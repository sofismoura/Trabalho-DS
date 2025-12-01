package com.mycompany.menuprodutos;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.util.Properties;
import java.io.File;

public class EmailSender {

    public static void enviarEmailComDetalhes(String destinatario, String nomeCliente, String detalhesPedido, double precoTotal) {
        final String remetente = "sofiatestejava@gmail.com";
        final String senha = "qste bpuz okxv qxhu";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        try {
            Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(remetente, senha);
                    }
                });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(remetente));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject("Confirmação do Pedido - Livraria Entre Palavras");

            // Criar a parte multipart
            Multipart multipart = new MimeMultipart();

            // Parte do texto (HTML)
            MimeBodyPart textPart = new MimeBodyPart();
            String htmlContent = criarConteudoEmail(nomeCliente, detalhesPedido, precoTotal);
            textPart.setContent(htmlContent, "text/html; charset=utf-8");
            multipart.addBodyPart(textPart);

            // Parte da imagem (QR Code) - se existir
            MimeBodyPart imagePart = new MimeBodyPart();
            String imagePath = "src/assets/images/QRcode1.png";
            File qrCodeFile = new File(imagePath);
            
            if (qrCodeFile.exists()) {
                try {
                    imagePart.attachFile(qrCodeFile);
                    imagePart.setContentID("<qrCodeImage>");
                    imagePart.setDisposition(MimeBodyPart.INLINE);
                    multipart.addBodyPart(imagePart);
                } catch (Exception e) {
                    System.err.println("Erro ao anexar QR Code: " + e.getMessage());
                    // Continua sem a imagem se houver erro
                }
            } else {
                System.out.println("QR Code não encontrado em: " + imagePath);
            }

            message.setContent(multipart);

            // Enviar email
            Transport.send(message);
            System.out.println("✅ Email com detalhes do pedido enviado com sucesso para: " + destinatario);
            
        } catch (MessagingException e) {
            System.err.println("❌ Erro ao enviar email: " + e.getMessage());
            throw new RuntimeException("Falha no envio do email: " + e.getMessage());
        }
    }

    
    public static void enviarEmailRecuperacaoSenha(String destinatario, String assunto, String mensagemHtml) {
        final String remetente = "sofiatestejava@gmail.com";
        final String senha = "qste bpuz okxv qxhu";

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        try {
            Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(remetente, senha);
                    }
                });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(remetente));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject(assunto);

            // Configurar o conteúdo HTML do email
            MimeBodyPart textPart = new MimeBodyPart();
            textPart.setContent(mensagemHtml, "text/html; charset=utf-8");

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(textPart);

            message.setContent(multipart);

            // Enviar email
            Transport.send(message);
            System.out.println("✅ Email de recuperação enviado com sucesso para: " + destinatario);
            
        } catch (MessagingException e) {
            System.err.println("❌ Erro ao enviar email de recuperação: " + e.getMessage());
            throw new RuntimeException("Falha no envio do email de recuperação: " + e.getMessage());
        }
    }

    private static String criarConteudoEmail(String nomeCliente, String detalhesPedido, double precoTotal) {
        return "<!DOCTYPE html>" +
               "<html lang='pt-BR'>" +
               "<head>" +
               "    <meta charset='UTF-8'>" +
               "    <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
               "    <title>Confirmação de Pedido</title>" +
               "    <style>" +
               "        body { font-family: 'Arial', sans-serif; line-height: 1.6; color: #333; margin: 0; padding: 20px; background-color: #f9f5f0; }" +
               "        .container { max-width: 600px; margin: 0 auto; background: white; padding: 30px; border-radius: 10px; box-shadow: 0 2px 10px rgba(0,0,0,0.1); }" +
               "        .header { text-align: center; background: linear-gradient(135deg, #8B4513, #A0522D); color: white; padding: 20px; border-radius: 10px 10px 0 0; margin: -30px -30px 20px -30px; }" +
               "        .header h1 { margin: 0; font-size: 24px; }" +
               "        .logo { font-size: 28px; font-weight: bold; margin-bottom: 10px; }" +
               "        .content { padding: 20px 0; }" +
               "        .detalhes-pedido { background-color: #f9f5f0; padding: 20px; border-radius: 8px; margin: 15px 0; border-left: 4px solid #8B4513; }" +
               "        .total { background-color: #8B4513; color: white; padding: 15px; border-radius: 8px; text-align: center; font-size: 18px; font-weight: bold; margin: 20px 0; }" +
               "        .qrcode-section { text-align: center; margin: 25px 0; padding: 20px; background-color: #f0f8ff; border-radius: 8px; }" +
               "        .footer { text-align: center; margin-top: 30px; padding-top: 20px; border-top: 2px solid #eee; color: #666; font-size: 12px; }" +
               "        .item-lista { margin: 8px 0; padding: 8px; background: white; border-radius: 5px; }" +
               "        .destaque { color: #8B4513; font-weight: bold; }" +
               "    </style>" +
               "</head>" +
               "<body>" +
               "    <div class='container'>" +
               "        <div class='header'>" +
               "            <div class='logo'>📚 Livraria Entre Palavras</div>" +
               "            <h1>Seu Pedido foi Confirmado!</h1>" +
               "        </div>" +
               "        " +
               "        <div class='content'>" +
               "            <h2>Olá, <span class='destaque'>" + nomeCliente + "</span>!</h2>" +
               "            <p>Seu pedido foi recebido com sucesso e está sendo processado.</p>" +
               "            " +
               "            <div class='detalhes-pedido'>" +
               "                <h3 style='color: #8B4513; margin-top: 0;'>📦 Detalhes do Pedido</h3>" +
               "                " + detalhesPedido +
               "            </div>" +
               "            " +
               "            <div class='total'>" +
               "                💰 Total do Pedido: " + String.format("R$ %.2f", precoTotal) +
               "            </div>" +
               "            " +
               "            <div class='qrcode-section'>" +
               "                <h3 style='color: #8B4513;'>📱 Pagamento PIX</h3>" +
               "                <p>Escaneie o QR Code abaixo para realizar o pagamento:</p>" +
               "                <img src='cid:qrCodeImage' alt='QR Code PIX' style='max-width: 250px; border: 3px solid #8B4513; border-radius: 12px; padding: 10px; background: white;'>" +
               "                <p style='margin-top: 15px; font-size: 14px; color: #555;'>" +
               "                    <strong>Chave PIX:</strong> contato@livrariaentrepalavras.com.br<br>" +
               "                    <strong>Valor:</strong> " + String.format("R$ %.2f", precoTotal) +
               "                </p>" +
               "            </div>" +
               "            " +
               "            <div style='background-color: #fff8e1; padding: 15px; border-radius: 8px; margin: 20px 0;'>" +
               "                <h4 style='color: #8B4513; margin-top: 0;'>📋 Próximos Passos</h4>" +
               "                <ol style='text-align: left;'>" +
               "                    <li>Realize o pagamento via PIX</li>" +
               "                    <li>Envie o comprovante para nosso WhatsApp</li>" +
               "                    <li>Acompanhe o status do seu pedido</li>" +
               "                    <li>Prepare-se para uma experiência literária incrível!</li>" +
               "                </ol>" +
               "            </div>" +
               "        </div>" +
               "        " +
               "        <div class='footer'>" +
               "            <p><strong>Livraria Entre Palavras</strong></p>" +
               "            <p>📞 (11) 99999-9999 | 📧 contato@livrariaentrepalavras.com.br</p>" +
               "            <p>🕒 Horário de atendimento: Segunda a Sábado, 9h às 18h</p>" +
               "            <p style='font-size: 11px; color: #999; margin-top: 15px;'>" +
               "                Este é um email automático, por favor não responda.<br>" +
               "                Se tiver dúvidas, entre em contato conosco pelos canais acima." +
               "            </p>" +
               "        </div>" +
               "    </div>" +
               "</body>" +
               "</html>";
    }

   
    public static void enviarEmail(String destinatario) {
        enviarEmailComDetalhes(destinatario, "Cliente", 
            "<p>Seu pedido foi processado com sucesso!</p>", 0.0);
    }
}