var express = require('express');
var router = express.Router();
var jwt = require('jsonwebtoken');
const ManualLymphDrainageMassage = require('../models/manual_lymph_drainage_massage');
var User = require('../models/user');

var authMiddleware = async (req, res, next) => {
  const authHeader = req.headers.authorization;
  if (authHeader) {
      const token = authHeader.split(' ')[1];
      try {
        var decoded = jwt.verify(token, 'static-secret');
        req.user = await User.findById(decoded.userID);
        next();
      } catch (e) {
        return res.status(403);
      }

  } else {
      res.sendStatus(401);
  }
}

/* GET home page. */
router.get('/', (req, res, next) => {
  res.render('index', { title: 'Selfcare Management backend server' });
});


router.post('/login', async (req, res, next) => {
  var user = await User.findOne({ username : req.body.username });
  if (user === null) { 
    return res.status(400).send({ 
        message : "User not found."
    }); 
  }

  if (user.validPassword(req.body.password)) { 
    var token = jwt.sign({userID: user.id}, 'static-secret', {expiresIn: '2h'});
    return res.status(200).send({ token: token });
  } else { 
    return res.status(400).send({ 
        message : "Wrong Password"
    });
  }
})


router.post('/sync', authMiddleware, async (req, res, next) => {
  var body = req.body;
  var user = req.user;
  try {
    if(body.mld != null) {
      await ManualLymphDrainageMassage.deleteMany({ user: user._id });
      for(var mld of body.mld) {
        mld = new ManualLymphDrainageMassage(mld);
        mld.user = req.user._id;
        await mld.save();
      }
      // Do the same thing for the rest of the treatment
    }
    return res.status(200).send({ message: "Treatment synced"});
  } catch(e) {
    return res.status(400).send(e);
  }

})

router.get('/:username', async (req, res, next) => {
  var username = req.params.username;
  if(!username) return res.status(400);
  var user = await User.findOne({ username: username });
  if(!user) return res.status(400);
  var mldRecords = await ManualLymphDrainageMassage.find({ user: user._id });
  return res.render("table", {
    title: "Treatment data",
    mldRecords: mldRecords
  });
})



// router.get('/business', authMiddleware, async (req, res, next) => {
//   try {
//     var businesses = [];
//     if(req.query._id) {
//       businesses = await Business.find({ _id: req.query._id });
//     } else {
//       businesses = await Business.find({});
//     }
//     return res.status(200).send(JSON.stringify(businesses));
//   } catch(e) {
//     return res.status(500);
//   }
// })

// router.post('/business', authMiddleware, async (req, res, next) => {
//   try {
//     await Business.create(req.body);
//     return res.status(201).json({ message: 'Business created '});
//   } catch (e) {
//     return res.status(500);
//   }  
// })


// router.put('/business/:id', authMiddleware, async (req, res, next) => {
//   try {
//     var id = req.params.id;
//     var business = await Business.findById(id);
//     if(!business) return res.status(404);
//     var body = req.body;
//     business.name = body.name;
//     business.email = body.email;
//     business.location = body.location;

//     await business.save();
//     return res.status(204).json({ message: 'Business updated '});
//   } catch(e) {
//     return res.status(500);
//   }
// })

// router.delete('/business/:id', authMiddleware, async (req, res, next) => {
//   try {
//     var id = req.params.id;
//     await Business.remove({ _id: id });
//     return res.status(200).json({ message: 'Business deleted '});
//   } catch(e) {
//     return res.status(500);
//   }
// })

// router.post('/visit', async(req, res, next) => {
//   try {
//     await Visit.create({ location: { type: 'Point', coordinates: [ req.body.latitude, req.body.longitude ] } });
//     return res.status(200).json({ message: 'Visit recorded '});
//   } catch(e) {
//     return res.status(500);
//   }
// })

// router.get('/visit',  async(req, res, next) => {
//   try {
//     var visits = await Visit.find({ });
//     return res.status(200).send(JSON.stringify(visits));
//   } catch(e) {
//     return res.status(500);
//   }
// })

module.exports = router;
