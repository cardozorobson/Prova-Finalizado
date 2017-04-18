/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author robsoncardozo
 */
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.Security;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.cms.CMSTypedData;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.bouncycastle.util.Store;


public class assinaBc {
    
    
    
    
    
    private static final String PATH_TO_KEYSTORE = "/home/robsoncardozo/Documentos/TesteSoluti/SolutiTeste/novo_cert.pfx";
    private static final String KEY_ALIAS_IN_KEYSTORE = "1";
    private static final String KEYSTORE_PASSWORD = "123456";
    private static final String SIGNATUREALGO = "SHA256withRSA";

    public assinaBc() {
    }
    
    KeyStore loadKeyStore() throws Exception {

        KeyStore keystore = KeyStore.getInstance("PKCS12");
        InputStream is = new FileInputStream(PATH_TO_KEYSTORE);
        keystore.load(is, KEYSTORE_PASSWORD.toCharArray());
        String alias = keystore.aliases().nextElement();
        
        return keystore;
    }
    
    CMSSignedDataGenerator setUpProvider(final KeyStore keystore) throws Exception {

        Security.addProvider(new BouncyCastleProvider());

        Certificate[] certchain = (Certificate[]) keystore.getCertificateChain(KEY_ALIAS_IN_KEYSTORE);

        final List<Certificate> certlist = new ArrayList<>();

        for (int i = 0, length = certchain == null ? 0 : certchain.length; i < length; i++) {
            certlist.add(certchain[i]);
        }
        Store certstore = new JcaCertStore(certlist);

        Certificate cert = keystore.getCertificate(KEY_ALIAS_IN_KEYSTORE);
        
        

        ContentSigner signer = new JcaContentSignerBuilder(SIGNATUREALGO).setProvider("BC").
                build((PrivateKey) (keystore.getKey(KEY_ALIAS_IN_KEYSTORE, KEYSTORE_PASSWORD.toCharArray())));

        CMSSignedDataGenerator generator = new CMSSignedDataGenerator();

        generator.addSignerInfoGenerator(new JcaSignerInfoGeneratorBuilder(new JcaDigestCalculatorProviderBuilder().setProvider("BC").
                build()).build(signer, (X509Certificate) cert));

        generator.addCertificates(certstore);

        return generator;
    }

    byte[] signPkcs7(final byte[] content, final CMSSignedDataGenerator generator) throws Exception {

        CMSTypedData cmsdata = new CMSProcessableByteArray(content);
        CMSSignedData signeddata = generator.generate(cmsdata, true);
        return signeddata.getEncoded();
    }
    
    
    
    
    
}
